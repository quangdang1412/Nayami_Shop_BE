package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.AddressRequestDTO;
import com.apinayami.demo.dto.response.AddressResponseDTO;

import java.util.List;

public interface IAddressService {

    AddressResponseDTO saveAddress(AddressRequestDTO address);

    AddressResponseDTO updateAddress(Long id, AddressRequestDTO address);

    void deleteAddress(Long id, String email);

    AddressResponseDTO getAddressById(Long id, String email);

    List<AddressResponseDTO> getAddressByCustomerId(Long id);


}