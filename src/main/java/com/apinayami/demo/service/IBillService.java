package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPayment;
import com.apinayami.demo.dto.response.BillResponseDTO;

public interface IBillService {
    Object createBill(String email,BillRequestDTO billRequest);
    BillResponseDTO getBill(String email,CartPayment request);
} 
