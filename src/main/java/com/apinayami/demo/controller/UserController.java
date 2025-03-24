package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.service.IBrandService;
import com.apinayami.demo.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
//@RequestMapping("/api/users")
@RequestMapping("api/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController implements Serializable {

    private final IUserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseData<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.create(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success","Add user successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user failed");
        }
    }
//
//    @SuppressWarnings("unchecked")
    @PutMapping("/{id}")
    public ResponseData<String> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            log.info("Request update brand: {}",userDTO.getUserName());

            //check
            UserDTO userExistWithId = userService.getUserById(id);
            if (userExistWithId == null) {
                return new ResponseError(HttpStatus.NOT_FOUND.value(), "User not found");
            }
             userService.update(userDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "update successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update failed");
        }
    }
//
//    @SuppressWarnings("unchecked")
    @DeleteMapping("/{id}")
    public ResponseData<String> deleteUser(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            if (user == null) {
                return new ResponseError(HttpStatus.NOT_FOUND.value(), "User not found");
            }
            userService.delete(user);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "delete successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete failed");
        }
    }
}
