package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.UserDTO;
import com.google.cloud.storage.Acl.User;

import java.util.List;

public interface IUserService extends IBaseCRUD<UserDTO> {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
}