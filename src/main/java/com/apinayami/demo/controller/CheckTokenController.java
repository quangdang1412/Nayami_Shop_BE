package com.apinayami.demo.controller;

import com.apinayami.demo.dto.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class CheckTokenController {
    @PostMapping("/api/check-token-admin")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
    public ResponseData<String> checkTokenAdmin()
    {
        return new ResponseData<>(HttpStatus.OK.value(), "Admin retrieved successfully");
    }

//    @PostMapping("/api/check-token-staff")
//    public ResponseData<String> checkTokenStaff()
//    {
//        return new ResponseData<>(HttpStatus.OK.value(), "Staff retrieved successfully");
//    }

    @PostMapping("/api/check-token-customer")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseData<String> checkTokenCustomer()
    {
        return new ResponseData<>(HttpStatus.OK.value(), "Customer retrieved successfully");
    }
}
