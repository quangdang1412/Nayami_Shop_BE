package com.apinayami.demo.dto.response;

import java.util.List;

import com.apinayami.demo.dto.request.CartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {
    private Double discount;
    private List< AddressResponseDTO> listAddress;
    private Long coupon;
    private List<CartItemDTO> listCartItem;
}