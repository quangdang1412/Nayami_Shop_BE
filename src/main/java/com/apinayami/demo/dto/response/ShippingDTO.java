package com.apinayami.demo.dto.response;

import com.apinayami.demo.model.AddressModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDTO implements Serializable {
    private AddressResponseDTO address;
    private double shippingFee;
}
