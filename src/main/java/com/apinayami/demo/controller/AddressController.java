package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.AddressRequestDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.service.IAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    
    private final IAddressService addressService;
    
    @GetMapping
    public ResponseData<?> getAllAddresses(@AuthenticationPrincipal UserModel user) {
        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem giỏ hàng");
        }
        
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy dữ liệu thành công", addressService.getAllAddresses());
    }
    
    @GetMapping("/{id}")
    public ResponseData<?> getAddressById(@AuthenticationPrincipal UserModel user,@PathVariable Long id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy địa chỉ thành công", addressService.getAddressById(id));
    }
    
    @PostMapping
    public ResponseEntity<ResponseData<?>> createAddress(
            @AuthenticationPrincipal UserModel user, 
            @Valid @RequestBody AddressRequestDTO addressDto) {
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập"));
        }

    addressDto.setUserId(user.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseData<>(HttpStatus.CREATED.value(), "Tạo địa chỉ thành công", addressService.saveAddress(addressDto)));
}

    
    @PutMapping("/{id}")
    public ResponseData<?> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO addressDto) {
        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật địa chỉ thành công", addressService.updateAddress(id, addressDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Xóa địa chỉ thành công", null);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseData<?> getAddressesByUserId(@PathVariable Long userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách địa chỉ theo User ID thành công", addressService.getAddressByCustomerId(userId));
    }
}
