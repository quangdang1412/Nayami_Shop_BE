package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.PromotionDTO;
import com.apinayami.demo.model.PromotionModel;

import java.util.List;

public interface IPromotionService extends IBaseCRUD<PromotionDTO>{
    List<PromotionDTO> getAllPromotions();
    PromotionDTO getPromotionById(long id);
}
