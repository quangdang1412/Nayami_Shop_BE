package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BillRequestDTO;

public interface IBillService {
    Object createBill(String email,BillRequestDTO billRequest);
} 
