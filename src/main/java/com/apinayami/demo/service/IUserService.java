package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.UserModel;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface IUserService extends IBaseCRUD<UserDTO> {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
}
