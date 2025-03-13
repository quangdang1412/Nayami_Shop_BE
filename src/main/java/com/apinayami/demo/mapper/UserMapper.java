package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.util.Enum.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
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
