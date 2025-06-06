package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.CreateCouponRequest;
import com.apinayami.demo.dto.response.CouponDto;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.CouponMapper;
import com.apinayami.demo.model.CouponModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICouponRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.ICouponService;
import com.apinayami.demo.util.Enum.ETypeCoupon;
import com.apinayami.demo.util.Singleton.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements ICouponService {

    private final ICouponRepository couponRepository;
    private final IUserRepository userRepository;
    private final CouponMapper couponMapper;

    public List<CouponDto> getAllCoupons() {
        return couponMapper.toDtoList(couponRepository.findAll());
    }

    public CouponDto getCouponById(String id) {
        return couponRepository.findById(id)
                .map(couponMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
    }

    public CouponDto getIdIsActive(String id) {
        return couponRepository.findByIdAndActiveTrue(id)
                .map(couponMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
    }

    public List<CouponDto> getCouponsByCustomerId(Long customerId) {
        UserModel customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return couponMapper.toDtoList(couponRepository.findByCustomerModel(customer));
    }

    public List<CouponDto> getCouponsByEmail(String email) {
        UserModel customer = userRepository.findByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with email: " + email);

        }

        return couponMapper.toDtoList(couponRepository.findByCustomerModel(customer));
    }

    public List<CouponDto> getActiveCouponsByCustomerId(Long customerId) {
        UserModel customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return couponMapper.toDtoList(couponRepository.findByCustomerModelAndActive(customer, true));
    }

    public List<CouponDto> getCouponsByType(ETypeCoupon type) {
        return couponMapper.toDtoList(couponRepository.findByType(type));
    }

    @Transactional
    public CouponDto createCoupon(CreateCouponRequest request) {
        String generatedId = IdGenerator.getInstance().generateId();
        CouponModel coupon = CouponModel.builder()
                .id(generatedId)
                .content(request.getContent())
                .value(request.getValue())
                .type(request.getType())
                .constraintMoney(request.getConstraintMoney())
                .active(request.isActive())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        if (request.getCustomerId() != null) {
            UserModel customer = userRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Customer not found with id: " + request.getCustomerId()));
            coupon.setCustomerModel(customer);
        }

        CouponModel savedCoupon = couponRepository.save(coupon);
        return couponMapper.toDto(savedCoupon);
    }

    @Transactional
    public CouponDto updateCoupon(String id, CreateCouponRequest request) {
        CouponModel coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));

        coupon.setContent(request.getContent());
        coupon.setValue(request.getValue());
        coupon.setType(request.getType());
        coupon.setConstraintMoney(request.getConstraintMoney());
        coupon.setActive(request.isActive());
        coupon.setStartDate(request.getStartDate());
        coupon.setEndDate(request.getEndDate());

        if (request.getCustomerId() != null) {
            UserModel customer = userRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Customer not found with id: " + request.getCustomerId()));
            coupon.setCustomerModel(customer);
        }

        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Transactional
    public void deleteCoupon(String id) {
        CouponModel coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        if (coupon.isActive()) {
            coupon.setActive(false);
        } else {
            coupon.setActive(true);

        }
        couponRepository.save(coupon);
    }

}
