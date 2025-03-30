package com.apinayami.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.oauth2.jwt.Jwt;
import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.service.IBillService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bills")
@SecurityRequirement(name = "bearerAuth") 
public class BillController {
    private final IBillService billService;
    private final JwtConfig jwtConfig;

    private String extractUserEmail(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        Jwt decodedToken = jwtConfig.decodeToken(token);
        return decodedToken.getSubject(); 
    }

    @PostMapping("/checkout")
    public ResponseData<?> getBill(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CartPaymentDTO cartPayment) {

        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }

        BillResponseDTO response = billService.getBill(email, cartPayment);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy dữ liệu thành công", response);
    }

    @PostMapping
    public ResponseData<?> createBill(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody BillRequestDTO billRequestDTO) {

        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }

        Object response = billService.createBill(email, billRequestDTO);
        if (response instanceof String) {
            return new ResponseData<>(HttpStatus.OK.value(), "Thanh toán online", Map.of("paymentUrl", response));
        }

        return new ResponseData<>(HttpStatus.CREATED.value(), "Đặt hàng thành công", response);
    }

    @GetMapping("/history")
    public ResponseData<?> getBillHistory(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }

        List<HistoryOrderDTO> response = billService.getBillHistory(email);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy dữ liệu thành công", response);
    }
    @GetMapping("/callback")
    public ResponseData<?> handlePaymentCallback(@RequestHeader(value = "Authorization", required = false) String authHeader,@RequestParam Map<String, String> params) {
        String status = params.get("status");
        Long orderID = Long.parseLong(params.get("orderCode"));
        String cancel = params.get("cancel");

        System.out.println("Received Payment Callback: " + params);
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }
        if ("CANCELLED".equalsIgnoreCase(status) || "true".equalsIgnoreCase(cancel)) {
            billService.cancelBill(email,orderID);
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Xử lý thanh toán thất bại");
        }

        if ("SUCCESS".equalsIgnoreCase(status)) {
            billService.confirmBill(email,orderID);
            return new ResponseData<>(HttpStatus.OK.value(), "Xử lý thanh toán thành công");
        }
        return new ResponseData<>(HttpStatus.OK.value(), "Trạng thái thanh toán không hợp lệ");

    }
}
