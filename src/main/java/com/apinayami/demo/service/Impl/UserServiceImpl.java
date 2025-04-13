package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.ResetPasswordDTO;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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
            String hashedPassword = passwordEncoder.encode(userModel.getPassword());
            userModel.setPassword(hashedPassword);
            userModel.setActive(true);
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
    public boolean checkUserExistByEmail(String email) {
        UserModel userExist = userRepository.findByEmail(email);
        return userExist != null;
    }

    @Override
    public boolean updateUserPassword(ResetPasswordDTO resetPasswordDTO,String authHeader) {
        String email = getEmailFromToken(authHeader);
        if(email != "") {
            try{
                UserModel userModel = userRepository.findByEmail(email);
                userModel.setPassword(resetPasswordDTO.getNewPassword());
                UserDTO userDTO = userMapper.toDetailDto(userModel);
                update(userDTO);
                return true;
            }catch(Exception e){
                log.error("Error: {}", e.getMessage());
                return false;
            }
        }
        return false;
    }

    private final JwtDecoder jwtDecoder;
    private String getEmailFromToken(String authHeader){
        try {
            String token = authHeader.replace("Bearer ", "");

            Jwt decodedJwt = jwtDecoder.decode(token);

            String tokenEmail = decodedJwt.getSubject();
            String role = decodedJwt.getClaimAsString("roles");

            if (!"REFRESH_PASSWORD_TOKEN".equals(role)) {
                return "";
            }
            return tokenEmail;
        }catch (Exception e){
            log.error("Error: {}", e.getMessage());
            return "";
        }
    }
}
