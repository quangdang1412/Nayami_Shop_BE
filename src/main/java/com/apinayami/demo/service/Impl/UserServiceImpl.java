package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.AdminInformationUpdateDTO;
import com.apinayami.demo.dto.request.ResetPasswordDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.BrandMapper;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.model.BillModel;
import com.apinayami.demo.model.LineItemModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IBrandRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.IUserService;
import com.apinayami.demo.util.Enum.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class  UserServiceImpl implements IUserService {
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
            // Lấy user hiện tại trong DB
            UserModel existingUser = userRepository.findById(userDTO.getUserId())
                    .orElseThrow(() -> new CustomException("User không tồn tại"));

            // Kiểm tra trùng email (ngoại trừ chính người dùng hiện tại)
            if(userRepository.existsByEmailAndIdNot(userModel.getEmail(), userDTO.getUserId())) {
                throw new CustomException("Email đã tồn tại");
            }

            // Kiểm tra trùng số điện thoại (ngoại trừ chính người dùng hiện tại)
            if(userRepository.existsByPhoneNumberAndIdNot(userModel.getPhoneNumber(), userDTO.getUserId())) {
                throw new CustomException("Số điện thoại đã tồn tại");
            }
            if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
                userModel.setPassword(hashedPassword);
            } else {
                // Giữ lại password cũ nếu không update
                userModel.setPassword(existingUser.getPassword());
            }
            userRepository.save(userModel);
            return "Cập nhật thành công " +userDTO.getUserName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            if (e instanceof CustomException)
                throw new CustomException(e.getMessage());
            throw new CustomException("Update user failed");
        }
    }


    public String updateWithoutCheckExistEmailAndNumberphone(UserDTO userDTO) {
        try {
            UserModel existingUser = userRepository.findById(userDTO.getUserId())
                    .orElseThrow(() -> new CustomException("User không tồn tại"));
            existingUser.setActive(!existingUser.isActive());
            userRepository.save(existingUser);
            return "Cập nhật thành công " +userDTO.getUserName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            if (e instanceof CustomException)
                throw new CustomException(e.getMessage());
            throw new CustomException("Update user failed");
        }
    }
    public String updateAdmin(AdminInformationUpdateDTO adminDTO) {
        try {
            String hashedPassword = passwordEncoder.encode(adminDTO.getPassword());
            String email = adminDTO.getEmail();
            userRepository.updateInformationOfAdmin(email,hashedPassword);
            return "Cập nhật thành công thông tin admin";
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            if (e instanceof CustomException)
                throw new CustomException(e.getMessage());
            throw new CustomException("Update user failed");
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
        List<UserDTO> allUser = userRepository.findAll().stream()
                .map(userMapper::toDetailDto)
                .collect(Collectors.toList());
        return allUser;
    }
    public List<UserDTO> getAllUsersWithoutPassword(Role role) {
        List<UserDTO> allUser = userRepository.getAllByType(role).stream()
                .map(userMapper::toDetailDtoWithoutPassword)
                .collect(Collectors.toList());
        return allUser;
    }

    @Override
    public UserDTO getUserByIdAndRole(Long id,Role role) {
        UserModel userModel = userRepository.findByIdAndType(id,role);
        if(userModel == null) {
            return null;
        }
        return userMapper.toDetailDtoWithoutPassword(userModel);
    }
    @Override
    public UserDTO getUserByEmail(String email) {
        return userMapper.toDetailDtoWithoutPassword(userRepository.findByEmail(email));
    }

    @Override
    public boolean checkUserBoughtProduct(String userEmail, long proID) {
        try{
            return userRepository.checkUserPurchaseProduct(userEmail, proID);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public boolean checkUserExistByEmail(String email) {
        UserModel userExist = userRepository.findByEmail(email);
        return userExist != null;
    }

    @Override
    public boolean updateUserPassword(ResetPasswordDTO resetPasswordDTO, String authHeader) {
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


    /*
    * Is used for refresh
    * */
    private final JwtDecoder jwtDecoder;
    private String getEmailFromToken(String authHeader){
        try {
            String token = authHeader.replace("Bearer ", "");

            Jwt decodedJwt = jwtDecoder.decode(token);

            String tokenEmail = decodedJwt.getSubject();

            return tokenEmail;
        }catch (Exception e){
            log.error("Error: {}", e.getMessage());
            return "";
        }
    }
}
