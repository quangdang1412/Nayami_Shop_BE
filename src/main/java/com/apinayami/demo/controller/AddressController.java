package com.apinayami.demo.controller;

import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.request.AddressRequestDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.service.IAddressService;
import com.apinayami.demo.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    
    private final IAddressService addressService;
    private final IUserService userService;
    private final JwtConfig jwtConfig;

    private String extractUserEmail(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        Jwt decodedToken = jwtConfig.decodeToken(token);
        return decodedToken.getSubject(); 
    }
    
    @GetMapping
    public ResponseData<?> getAllAddresses(@RequestHeader("Authorization") String authHeader) {
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem danh sách địa chỉ");
        }
        UserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Người dùng không tồn tại");
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Lấy dữ liệu thành công", addressService.getAddressByCustomerId(user.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseData<?> getAddressById(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }

        var address = addressService.getAddressById(id);
        if (address == null) {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy địa chỉ");
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Lấy địa chỉ thành công", address);
    }

    @PostMapping
    public ResponseData<?> createAddress(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AddressRequestDTO addressDto) {
        
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }
        UserDTO user = userService.getUserByEmail(email);
        addressDto.setUserId(user.getUserId());
        var savedAddress = addressService.saveAddress(addressDto);

        return new ResponseData<>(HttpStatus.CREATED.value(), "Tạo địa chỉ thành công", savedAddress);
    }

    @PutMapping("/{id}")
    public ResponseData<?> updateAddress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDTO addressDto) {

        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }

        var updatedAddress = addressService.updateAddress(id, addressDto);
        if (updatedAddress == null) {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy địa chỉ để cập nhật");
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật địa chỉ thành công", updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deleteAddress(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }

        try {
            addressService.deleteAddress(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Xóa địa chỉ thành công");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy địa chỉ để xóa");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseData<?> getAddressesByUserId(@PathVariable Long userId) {
        var addresses = addressService.getAddressByCustomerId(userId);
        if (addresses == null || addresses.isEmpty()) {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy địa chỉ nào cho User ID: " + userId);
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách địa chỉ theo User ID thành công", addresses);
    }
}
