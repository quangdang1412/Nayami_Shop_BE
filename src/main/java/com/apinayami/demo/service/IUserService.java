package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.UserModel;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends IBaseCRUD<UserDTO> {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDetailsService userDetailService();

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    UserModel getUserByEmail(String email);
}
