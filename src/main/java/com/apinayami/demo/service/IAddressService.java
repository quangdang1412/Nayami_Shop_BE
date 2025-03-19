package com.apinayami.demo.service;

import java.util.List;

import com.apinayami.demo.dto.request.AddressRequestDTO;
import com.apinayami.demo.dto.response.AddressResponseDTO;
import com.apinayami.demo.model.AddressModel;

public interface IAddressService {

    AddressResponseDTO saveAddress(AddressRequestDTO address);

    AddressResponseDTO updateAddress(Long id, AddressRequestDTO address);

    void deleteAddress(Long id);

    AddressResponseDTO getAddressById(Long id);

    List<AddressResponseDTO> getAddressByCustomerId(Long id);

    List<AddressResponseDTO> getAllAddresses();
    
}
