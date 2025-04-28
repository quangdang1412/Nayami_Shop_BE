package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.ResetPasswordDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.util.Enum.Role;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface IUserService extends IBaseCRUD<UserDTO> {
    List<UserDTO> getAllUsers();
    UserDTO getUserByIdAndRole(Long id, Role role);
    boolean checkUserExistByEmail(String email);
    boolean updateUserPassword(ResetPasswordDTO resetPasswordDTO, String authHeader);
    UserDTO getUserByEmail(String email);
    boolean checkUserBoughtProduct(String userEmail, long proID);
}
