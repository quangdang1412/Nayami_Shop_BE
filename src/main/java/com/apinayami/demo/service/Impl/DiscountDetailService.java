package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.DiscountDetailDTO;
import com.apinayami.demo.model.DiscountDetailModel;
import com.apinayami.demo.repository.IDiscountDetailRepository;
import com.apinayami.demo.service.IDiscountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountDetailService implements IDiscountDetailService {
    private final IDiscountDetailRepository discountDetailRepository;

    @Override
    public String create(DiscountDetailDTO a) {
        return null;
    }

    @Override
    public String update(DiscountDetailDTO a) {
        return null;
    }

    @Override
    public String delete(DiscountDetailDTO a) {
        return null;
    }

    @Override
    public DiscountDetailModel findDiscountDetailById(long id) {
        return discountDetailRepository.findById(id).orElse(null);
    }
}
