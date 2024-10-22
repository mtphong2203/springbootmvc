package com.maiphong.springbootmvc.mapper.user;

import org.mapstruct.Mapper;

import com.maiphong.springbootmvc.dtos.Category.auth.RegisterRequestDTO;
import com.maiphong.springbootmvc.dtos.Category.auth.UserDTO;
import com.maiphong.springbootmvc.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    User toUser(RegisterRequestDTO registerRequestDTO);
}
