package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.DashBoardResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;
import com.apinayami.demo.util.Enum.EBillStatus;

import java.time.LocalDate;
import java.util.List;

public interface IBillService {
    Object createBill(String email, BillRequestDTO billRequest);

    BillResponseDTO getBill(String email, CartPaymentDTO request);

    List<HistoryOrderDTO> getBillHistory(String email);

    ;

    void cancelBill(String email, Long billId);

    void confirmBill(String email, Long billId);

    Long countBillsByStatus(EBillStatus status);

    Double totalRevenue(EBillStatus status);

    Double totalProfit(EBillStatus status);

    DashBoardResponseDTO getRevenueByTime(LocalDate startDate, LocalDate endDate, EBillStatus status);

    DashBoardResponseDTO getProfitByTime(LocalDate startDate, LocalDate endDate, EBillStatus status);

    
} 
