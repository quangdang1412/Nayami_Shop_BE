package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.AdminInformationUpdateDTO;
import com.apinayami.demo.dto.request.ResetPasswordDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.service.Impl.UserServiceImpl;
import com.apinayami.demo.util.Enum.Role;
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

    @GetMapping("/get-all-users")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsersWithoutPassword(Role.CUSTOMER);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserByIdAndRole(id, Role.CUSTOMER);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching user by id: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        try {
            UserDTO user = userService.getUserByEmail(email);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching user by email: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestBody Map<String, Object> requestBody) {
        try {
            UserDTO user = userService.getUserByEmail(requestBody.get("email").toString());
            System.out.println(user.getEmail());
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching user by email from request body: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    public ResponseData<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.create(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Add user successfully");
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
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

    /*
     * These controller is used for get all staffs
     * */
    @GetMapping("/get-all-staffs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllStaffs() {
        try {
            List<UserDTO> users = userService.getAllUsersWithoutPassword(Role.STAFF);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Update staff
    @GetMapping("/staff/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> getStaffById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserByIdAndRole(id, Role.STAFF);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching user by id: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/password/staff/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseData<String> updatePassword(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
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

    /*Update information of admin*/
    @PutMapping("/update/inform/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseData<String> updatePassword(@Valid @RequestBody AdminInformationUpdateDTO adminDTO) {
        try {
            userService.updateAdmin(adminDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "update successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update failed");
        }
    }


}
