package com.apinayami.demo.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;

public interface IBillService {
    Object createBill(String email,BillRequestDTO billRequest);
    BillResponseDTO getBill(String email,CartPaymentDTO request);
    List<HistoryOrderDTO> getBillHistory(String email); ;
    void cancelBill(String email,Long billId);
    void confirmBill(String email,Long billId);
} 
