package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.ResetPasswordDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.service.Impl.ResetPasswordService;
import com.apinayami.demo.service.Impl.UserServiceImpl;
import com.apinayami.demo.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Slf4j
@RequestMapping("/api/reset-password")
@RequiredArgsConstructor
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;
    @GetMapping
    public ResponseData<String> sendLinkResetPasswordToClient(@RequestParam String email) {
        try {
            boolean sendEmailSuccessfully = resetPasswordService.sendLinkResetPassword(email);
            if(sendEmailSuccessfully){
                return new ResponseData<>(HttpStatus.OK.value(),"Thành công","Gửi email thành công");
            }else{
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(),"Thất bại","Gửi email không thành công");
            }
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update failed");
        }
    }
    @GetMapping("/check-token")
    public ResponseData<String> checkToken() {
        return new ResponseData<>(HttpStatus.OK.value(),"Thành công","Token hợp lệ");
    }

    private final UserServiceImpl userService;
    @PostMapping("/entered")
    public ResponseData<String> resetPassword(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody ResetPasswordDTO resetPasswordDTO)
    {
        try {
            boolean isResetPassword =  userService.updateUserPassword(resetPasswordDTO,authHeader);
            if(isResetPassword){
                return new ResponseData<>(HttpStatus.OK.value(), "Thành công","Reset password thành công");
            }
            else{
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Cập nhật password không thành công");
            }
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Cập nhật password không thành công");
        }
    }
}
