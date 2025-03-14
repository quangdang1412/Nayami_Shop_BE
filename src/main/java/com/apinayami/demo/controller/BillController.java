package com.apinayami.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.util.Enum.EPaymentMethod;

import lombok.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bills")
public class BillController {
    private final IBillService billService;
    @PostMapping
    public ResponseData<?> createBill(@AuthenticationPrincipal UserModel user,
            @RequestBody BillRequestDTO billRequestDTO) {


        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem giỏ hàng");
        }
        BillResponseDTO response = billService.createBill(user.getEmail(),billRequestDTO);
        // Nếu là thanh toán BANKING, trả về thêm thông tin URL thanh toán
        if (billRequestDTO.getPaymentMethod() == EPaymentMethod.ONLINE_BANKING) {
            Map<String, Object> result = new HashMap<>();
            result.put("bill", response);
            result.put("paymentUrl", response.getPaymentUrl());
            result.put("message", "Vui lòng chuyển hướng khách hàng đến URL thanh toán");
            return new ResponseData<>(HttpStatus.OK.value(), "Tạo hóa đơn thành công", result);
        }
        
        // Nếu là COD, chỉ trả về thông tin hóa đơn
        return new ResponseData<>(HttpStatus.CREATED.value(), "Đặt hàng thành công",response);

    }
}
