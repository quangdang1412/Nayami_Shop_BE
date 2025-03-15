package com.apinayami.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPayment;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.service.IBillService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bills")
@SecurityRequirement(name = "bearerAuth") 
public class BillController {
    private final IBillService billService;

    @PostMapping("/checkout")
    public ResponseData<?>  getBill(@AuthenticationPrincipal UserModel user,
    @RequestBody CartPayment cartPayment) {


        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem giỏ hàng");
        }
        BillResponseDTO response = billService.getBill(user.getEmail(),cartPayment);



        return new ResponseData<>(HttpStatus.CREATED.value(), "Đặt hàng thành công",response);

    }
    




    @PostMapping
    public ResponseData<?> createBill(@AuthenticationPrincipal UserModel user,
            @RequestBody BillRequestDTO billRequestDTO) {


        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem giỏ hàng");
        }
        Object response = billService.createBill(user.getEmail(),billRequestDTO);
        if (response instanceof String) {
            return new ResponseData<>(HttpStatus.OK.value(), "Thanh toán online", Map.of("paymentUrl", response));
        }
        
        return new ResponseData<>(HttpStatus.CREATED.value(), "Đặt hàng thành công",response);

    }
}
