package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.SignupDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final IUserService userService;
    private final UserMapper userMapper;
    public boolean register(SignupDTO signupDTO) {
        try {
            if (signupDTO == null) {
                return false;
            }
            UserDTO userDTO = userMapper.convertSignupDtoToUserDto(signupDTO);
            userService.create(userDTO);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
