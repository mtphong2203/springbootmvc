package com.maiphong.springbootmvc.services;

import com.maiphong.springbootmvc.dtos.Category.auth.RegisterRequestDTO;
import com.maiphong.springbootmvc.dtos.Category.auth.UserDTO;

public interface AuthService {
    UserDTO register(RegisterRequestDTO registerRequestDTO);
}
