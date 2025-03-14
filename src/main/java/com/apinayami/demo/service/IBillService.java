package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;

public interface IBillService {
    BillResponseDTO createBill(String email,BillRequestDTO billRequest);
} 
