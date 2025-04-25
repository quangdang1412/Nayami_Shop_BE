package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.ResetPasswordDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.service.Impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController implements Serializable {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsersWithoutPassword();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<UserDTO> getUserByEmail(@RequestBody Map<String, Object> requestBody) {
        UserDTO user = userService.getUserByEmail(requestBody.get("email").toString());
        System.out.println(user.getEmail());
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseData<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.create(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Add user successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user failed");
        }
    }

    //    @SuppressWarnings("unchecked")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    public ResponseData<String> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            log.info("Request update user: {}", userDTO.getUserName());
            userService.update(userDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "update successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update failed");
        }
    }

    @PutMapping("/update-password")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseData<String> updateUser(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            boolean isResetPassword = userService.updateUserPassword(resetPasswordDTO, authHeader);
            if (isResetPassword) {
                return new ResponseData<>(HttpStatus.OK.value(), "Thành công", "Cập nhật password thành công");
            } else {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Cập nhật password không thành công");
            }
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update failed");
        }
    }

    //
//    @SuppressWarnings("unchecked")
    @DeleteMapping("/delete/{id}")
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

    @PostMapping("/check")
    public ResponseData<Boolean> checkUSerBoughtProduct(@RequestBody Map<String, Object> requestBody) {
        try {
            long proId = Long.parseLong(requestBody.get("proId").toString());
            String userEmail = requestBody.get("email").toString();
            return new ResponseData<>(HttpStatus.OK.value(), "Check customer bought product", userService.checkUserBoughtProduct(userEmail, proId));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
