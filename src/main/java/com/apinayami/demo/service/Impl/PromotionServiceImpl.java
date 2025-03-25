package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.PromotionDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.PromotionMapper;
import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.model.PromotionModel;
import com.apinayami.demo.repository.IImageRepository;
import com.apinayami.demo.repository.IPromotionRepository;
import com.apinayami.demo.service.IPromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceImpl implements IPromotionService {
    private final IPromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final IImageRepository imageRepository;

    public List<PromotionModel> getAllPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    public PromotionModel getPromotionById(long id) {
        return promotionRepository.findById(id).isPresent() ? promotionRepository.findById(id).get() : null;
    }

    @Override
    public String create(PromotionModel promotionModel) {
        try{
//        PromotionModel promotionModel = promotionMapper.toPromotionModel(promotionDTO);
        PromotionModel tempPromotionModel = promotionRepository.save(promotionModel);
        if(!promotionModel.getPromotionImages().isEmpty()){
            for (ImageModel image : promotionModel.getPromotionImages()) {
                image.setPromotionModel(tempPromotionModel);
                imageRepository.save(image);
            }
        }
        return "Thêm thành công" + promotionModel.getTitle();
        }
        catch (Exception e){
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Promotion has existed");
        }
    }

    @Override
    public String update(PromotionModel a) {
        try {
            promotionRepository.save(a);
            return "Cập nhật thành công " + a.getTitle();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Lỗi cập nhật");
        }
    }

    @Override
    public String delete(PromotionModel a) {
        try{
            promotionRepository.delete(a);
            return "Promotion has been deleted";
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
