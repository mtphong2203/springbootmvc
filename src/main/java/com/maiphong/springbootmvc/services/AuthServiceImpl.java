package com.maiphong.springbootmvc.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.springbootmvc.dtos.Category.auth.RegisterRequestDTO;
import com.maiphong.springbootmvc.dtos.Category.auth.UserDTO;
import com.maiphong.springbootmvc.entities.User;
import com.maiphong.springbootmvc.mapper.user.UserMapper;
import com.maiphong.springbootmvc.repositories.UserRepository;

@Service
@Transactional
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = Set.of();
        if (user.getRoles() != null) {
            authorities = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Override
    public UserDTO register(RegisterRequestDTO registerRequestDTO) {
        // Validate register request
        if (registerRequestDTO == null) {
            throw new IllegalArgumentException("Register request cannot be null");
        }

        // Check if username is already taken
        var existingUser = userRepository.findByUsername(registerRequestDTO.getUsername());

        if (existingUser != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Compare password and confirm password
        if (registerRequestDTO.getPassword() == null ||
                !registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Create a new user
        // var user = new User();
        // user.setFirstName(registerRequestDTO.getFirstName());
        // user.setLastName(registerRequestDTO.getLastName());
        // user.setUsername(registerRequestDTO.getUsername());
        // user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        var user = userMapper.toUser(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        // Save user to database
        user = userRepository.save(user);

        // Return user DTO
        // var userDTO = new UserDTO();
        // userDTO.setId(user.getId());
        // userDTO.setFirstName(user.getFirstName());
        // userDTO.setLastName(user.getLastName());
        // userDTO.setUsername(user.getUsername());
        // userDTO.setDisplayName(user.getDisplayName());
        var userDTO = userMapper.toUserDTO(user);

        return userDTO;
    }
}
