package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.DiscountDetailDTO;
import com.apinayami.demo.model.DiscountDetailModel;

public interface IDiscountDetailService extends IBaseCRUD<DiscountDetailDTO> {
    DiscountDetailModel findDiscountDetailById(long id);
}
