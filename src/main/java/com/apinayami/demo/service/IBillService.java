package com.apinayami.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPayment;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;
import com.apinayami.demo.model.BillModel;

public interface IBillService {
    Object createBill(String email,BillRequestDTO billRequest);
    BillResponseDTO getBill(String email,CartPayment request);
    Page<HistoryOrderDTO> getBillHistory(String email, Pageable pageable); ;
} 
