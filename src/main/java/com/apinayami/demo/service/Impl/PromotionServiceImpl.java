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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceImpl implements IPromotionService {
    private final IPromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final IImageRepository imageRepository;

    public List<PromotionDTO> getAllPromotions() {
        List<PromotionModel> promotionModelList = promotionRepository.findAll();
        return promotionModelList.stream().map(promotionMapper::toPromotionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PromotionDTO getPromotionById(long id) {
        return promotionRepository.findById(id).isPresent() ? PromotionMapper.INSTANCE.toPromotionDTO(promotionRepository.findById(id).get()) : null;
    }

    @Override
    public String create(PromotionDTO promotionDTO) {
        try{
            PromotionModel savedPromotion = promotionRepository.save(PromotionMapper.INSTANCE.toPromotionModel(promotionDTO));
            if(!promotionDTO.getPromotionImages().isEmpty()){
                for (ImageModel image : promotionDTO.getPromotionImages()) {
                    image.setPromotionModel(savedPromotion);
                    imageRepository.save(image);
                }
            }
            return "Thêm thành công" + savedPromotion.getTitle();
        }
        catch (Exception e){
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Promotion has existed");
        }
    }

    @Override
    public String update(PromotionDTO promotionDTO) {
        try {
            PromotionModel updated_model = promotionRepository.findById(promotionDTO.getId()).orElse(null);
            assert updated_model != null;
            updated_model.setTitle(promotionDTO.getTitle());
            updated_model.setDescription(promotionDTO.getDescription());
            updated_model.setStartDate(promotionDTO.getStartDate());
            updated_model.setEndDate(promotionDTO.getEndDate());
            updated_model.setDisplayStatus(promotionDTO.isDisplayStatus());
            PromotionModel savedPromotion = promotionRepository.save(updated_model);

            if(!promotionDTO.getPromotionImages().isEmpty()) {
                for (ImageModel image : promotionDTO.getPromotionImages()) {
                    image.setPromotionModel(savedPromotion);
                    imageRepository.save(image);
                }
            }
            return "Cập nhật thành công " + promotionDTO.getTitle();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Lỗi cập nhật");
        }
    }

    @Override
    public String delete(PromotionDTO promotionDTO) {
        try{
            promotionRepository.deleteById(promotionDTO.getId());
            return "Promotion has been deleted";
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
