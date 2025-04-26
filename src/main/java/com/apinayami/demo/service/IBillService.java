package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.dto.response.*;
import com.apinayami.demo.util.Enum.EBillStatus;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IBillService {
    Object createBill(String email, BillRequestDTO billRequest);

    BillResponseDTO getBill(String email, CartPaymentDTO request);

    List<HistoryOrderDTO> getBillHistory(String email);

    List<BillDTO> getAllBills();

    String cancelBill(String email, Long billId);

    void updateBill(String email, Long billId, String status) throws IOException;

    void confirmBill(String email, Long billId);

    Long countBillsByStatus(EBillStatus status);

    Double totalRevenue(EBillStatus status);

    Double totalProfit(EBillStatus status);
    
    BillDetailDTO getBillByID(Long id);

    String RequestGuarantee(String email, Long billId);

    List<ProductBestSellingDTO> getProductBestSellingByTime(LocalDate startDate, LocalDate endDate, EBillStatus status);

    DashBoardResponseDTO getRevenueOrProfitByTime(LocalDate startDate, LocalDate endDate, EBillStatus status, int a);
    
    String handlePayment(String email,Long id);

}
