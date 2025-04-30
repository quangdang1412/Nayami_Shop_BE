package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.AdminInformationUpdateDTO;
import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.dto.request.SignupDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.util.Enum.Role;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDTO convertSignupDtoToUserDto(SignupDTO signupDTO) {
        if(signupDTO == null) {
            return null;
        }
        return UserDTO.builder()
                .type(Role.CUSTOMER.name())
                .email(signupDTO.getEmail())
                .active(true)
                .phoneNumber(signupDTO.getPhoneNumber())
                .password(signupDTO.getPassword())
                .userName(signupDTO.getFullName())
                .build();
    }
    @Named("userWithPassword")
    public UserDTO toDetailDto(UserModel user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .userId(user.getId())
                .type(user.getType().toString())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .active(user.isActive())
                .password(user.getPassword())
                .userName(user.getUsername())
                .build();
    }
    @Named("userWithoutPassword")
    public UserDTO toDetailDtoWithoutPassword(UserModel user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .userId(user.getId())
                .type(user.getType().toString())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .active(user.isActive())
                .userName(user.getUsername())
                .build();
    }
    public UserModel toDetailModel(UserDTO user) {
        if (user == null) {
            return null;
        }
        return UserModel.builder()
                .id(user.getUserId())
                .type(Role.valueOf(user.getType()))
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .active(user.isActive())
                .password(user.getPassword())
                .userName(user.getUserName())
                .build();
    }

}
