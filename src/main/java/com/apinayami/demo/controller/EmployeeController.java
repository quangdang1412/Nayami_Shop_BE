package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.UserRequestDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.service.IAdminService;
import com.apinayami.demo.service.IStaffService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {
    private final IAdminService adminService;
    private final IStaffService staffService;

    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
    @PostMapping("/add-employee")
    public ResponseData<String> addUser(@Valid @RequestBody UserRequestDTO userDTO) {
        try {
            log.info("Request add user: {}", userDTO.getUserName());
            String userId = userDTO.getType().equals("ADMIN") ? adminService.create(userDTO) : staffService.create(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
}
