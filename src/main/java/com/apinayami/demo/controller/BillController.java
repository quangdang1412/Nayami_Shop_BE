package com.apinayami.demo.controller;

import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.dto.response.*;
import com.apinayami.demo.service.IBillService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
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

        try {
            String email = extractUserEmail(authHeader);
            if (email == null) {
                return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
            }

            Object response = billService.createBill(email, billRequestDTO);
            if (response instanceof String) {
                return new ResponseData<>(HttpStatus.OK.value(), "Thanh toán online", Map.of("paymentUrl", response));
            }

            return new ResponseData<>(HttpStatus.CREATED.value(), "Đặt hàng thành công", response);

        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đặt hàng thất bại", e.getMessage());
        }
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
    public ResponseData<?> handlePaymentCallback(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestParam Map<String, String> params) {
        String status = params.get("status");
        Long orderID = Long.parseLong(params.get("orderCode"));
        String cancel = params.get("cancel");

        System.out.println("Received Payment Callback: " + params);
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập");
        }
        if ("CANCELLED".equalsIgnoreCase(status) || "true".equalsIgnoreCase(cancel)) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Xử lý thanh toán thất bại");
        }

        if ("SUCCESS".equalsIgnoreCase(status)) {
            billService.confirmBill(email, orderID);
            return new ResponseData<>(HttpStatus.OK.value(), "Xử lý thanh toán thành công");
        }
        return new ResponseData<>(HttpStatus.OK.value(), "Trạng thái thanh toán không hợp lệ");

    }

    @PostMapping("/cancel")
    public ResponseData<?> cancelBill(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                      @RequestBody Map<String, Object> billID) {
        String email = extractUserEmail(authHeader);
        try {
            String result = billService.cancelBill(email, Long.parseLong(billID.get("billID").toString()));
            return new ResponseData<>(HttpStatus.OK.value(), result);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/status")
    public ResponseData<?> updateStatus(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                        @RequestBody Map<String, Object> billUpdate) {
        String email = billUpdate.get("email").toString();
        try {
            System.out.println("Received billID: " + billUpdate.get("billID"));
            billService.updateBill(email, Long.parseLong(billUpdate.get("billID").toString()), billUpdate.get("status").toString());
            return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật đơn hàng thành công");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @GetMapping()
    public ResponseData<?> getAllBill() {
//        try{
        List<BillDTO> billDTOList = billService.getAllBills();
        return new ResponseData<>(HttpStatus.OK.value(), "Danh sách đơn hàng", billDTOList);
//        }
//        catch (Exception e) {
//            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
//        }
    }

    @GetMapping("/{id}")
    public ResponseData<?> getBillById(@PathVariable("id") Long id) {
        BillDetailDTO billDetail = billService.getBillByID(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Đơn hàng", billDetail);
    }

    @PostMapping("/guarantee")
    public ResponseData<?> RequestGuarantee(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                            @RequestBody Map<String, Object> billID) {
        String email = extractUserEmail(authHeader);
        try {
            log.info("Received billID: " + billID.get("billID"));
            billService.RequestGuarantee(email, Long.parseLong(billID.get("billID").toString()));
            return new ResponseData<>(HttpStatus.OK.value(), "Yêu cầu bảo hành thành công");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @PostMapping("/payment/{id}")
    public ResponseData<?> handlePayment(@RequestHeader(value = "Authorization", required = false) String authHeader,@PathVariable Long id) {
        String email = extractUserEmail(authHeader);
        try {
            Object response = billService.handlePayment(email, id);
            return new ResponseData<>(HttpStatus.OK.value(), "Payment URL generated", Map.of("paymentUrl", response));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

}
