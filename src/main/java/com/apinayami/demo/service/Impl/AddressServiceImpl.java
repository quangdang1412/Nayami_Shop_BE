package com.apinayami.demo.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apinayami.demo.dto.request.AddressRequestDTO;
import com.apinayami.demo.dto.response.AddressResponseDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.AddressMapper;
import com.apinayami.demo.model.AddressModel;
import com.apinayami.demo.repository.IAddressRepository;
import com.apinayami.demo.service.IAddressService;
import com.google.cloud.storage.Acl.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService  {
    private final IAddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDTO saveAddress(AddressRequestDTO address) {
        log.info("Saving address: {}", address);
        AddressModel savedModel = addressMapper.toModel(address);
        savedModel.setActive(true);
        addressRepository.save(savedModel);
        log.info("Address saved successfully: {}", savedModel);

        return addressMapper.toResponseDTO(savedModel);
    }

    @Override
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO requestDTO) {
        log.info("Updating address with ID: {}", id);
        AddressModel address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
            addressMapper.updateAddressFromDto(requestDTO, address);
        AddressModel updatedAddress = addressRepository.save(address);

        return addressMapper.toResponseDTO(updatedAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        log.info("Deleting address with ID: {}", id);
        AddressModel address = addressRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        address.setActive(false);
        addressRepository.save(address);
    }

    @Override
    public AddressResponseDTO getAddressById(Long id) {
        log.info("Fetching address with ID: {}", id);
        AddressModel addressModel = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return addressMapper.toResponseDTO(addressModel);
    }
    @Override
    public List<AddressResponseDTO> getAddressByCustomerId(Long CustomerId) {
        log.info("Fetching address with ID: {}", CustomerId);
        List<AddressModel> addresses = addressRepository.findByCustomerModel_IdAndActiveTrue(CustomerId);
        return addresses.stream()
                .map(addressMapper::toResponseDTO)
                .toList();
    }


}
