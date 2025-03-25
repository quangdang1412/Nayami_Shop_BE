package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.BrandMapper;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IBrandRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String create(UserDTO userDTO) {
        try {
            UserModel userModel = userMapper.toDetailModel(userDTO);
            /*
            * Tránh trường hợp lúc chèn xuống lỗi trùng nó vẫn tăng key lên.
            * */
            if(userRepository.existsByEmail(userModel.getEmail())) {
                throw new CustomException("Email đã tồn tại");
            }
            if(userRepository.existsByPhoneNumber(userModel.getPhoneNumber())) {
                throw new CustomException("Số điện thoại đã tồn tại");
            }

            //hash password
            String hashedPassword = passwordEncoder.encode(userModel.getPassword());
            userModel.setPassword(hashedPassword);
            userModel.setActive(true);
            userRepository.save(userModel);
            return "Thêm thành công "+userModel.getType()+" "+ userModel.getId();

        } catch (Exception e) {
            if (e instanceof CustomException)
                throw new CustomException(e.getMessage());
            throw new CustomException("Add user failed");
        }
    }


    @Override
    public String update(UserDTO userDTO) {
        try {
            UserModel userModel = userMapper.toDetailModel(userDTO);

            if(userModel != null){
                userRepository.save(userModel);
            }
            return "Cập nhật thành công " +userDTO.getUserName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return "Lỗi khi cập nhật " + userDTO.getUserName();
        }
    }

    @Override
    public String delete(UserDTO userDTO) {
        try {
            UserModel userModel = userMapper.toDetailModel(userDTO);

            if(userModel != null){
                userRepository.delete(userModel);
            }
            return "Xóa thành công " +userDTO.getUserName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return "Lỗi khi xóa " + userDTO.getUserName();
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        UserModel userModel = userRepository.findById(id).orElse(null);
        if(userModel == null) {
            return null;
        }
        return userMapper.toDetailDto(userModel);
    }
    @Override
    public UserDTO getUserByEmail(String email) {
        return userMapper.toDetailDto(userRepository.findByEmail(email));
    }
}