package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.SignupDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl {
    private final IUserService userService;
    private final UserMapper userMapper;

    public boolean register(SignupDTO signupDTO) {
        if (signupDTO == null) {
            throw new CustomException("Thông tin đăng ký không được để trống");
        }
        try {
            UserDTO userDTO = userMapper.convertSignupDtoToUserDto(signupDTO);
            userService.create(userDTO);
            return true;
        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        } catch (Exception e) {
            throw new CustomException("Đăng ký không thành công");
        }
    }
}
