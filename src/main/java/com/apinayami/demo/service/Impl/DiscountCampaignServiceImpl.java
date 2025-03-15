package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.DiscountCampaignDTO;
import com.apinayami.demo.dto.request.DiscountDetailDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.DiscountCampaignMapper;
import com.apinayami.demo.model.DiscountCampaignModel;
import com.apinayami.demo.model.DiscountDetailModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.repository.IDiscountCampaignRepository;
import com.apinayami.demo.repository.IDiscountDetailRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.service.IDiscountCampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountCampaignServiceImpl implements IDiscountCampaignService {
    private final IDiscountCampaignRepository discountCampaignRepository;
    private final IDiscountDetailRepository discountDetailRepository;
    private final IProductRepository productRepository;
    private final DiscountCampaignMapper discountCampaignMapper;

    @Override
    public String create(DiscountCampaignDTO a) {
        List<DiscountDetailModel> discountDetailModelList = new ArrayList<>();
        for (DiscountDetailDTO x : a.getDiscountDetailDTOList()) {
            DiscountDetailModel discountDetailModel = DiscountDetailModel.builder()
                    .percentage(x.getPercentage())
                    .build();
            List<ProductModel> productModelList = new ArrayList<>();
            for (long proId : x.getProductID()) {
                productModelList.add(productRepository.findById(proId).orElse(null));
            }

            discountDetailModel.setListProduct(productModelList);
            if (x.getId() != 0)
                discountDetailModel.setId(x.getId());
            discountDetailModelList.add(discountDetailModel);
        }
        DiscountCampaignModel discountCampaignModel;
        if (a.getId() != 0) {
            discountCampaignModel = discountCampaignRepository.findById(a.getId()).orElseThrow(() -> new RuntimeException("Configuration not found"));

        } else {
            discountCampaignModel = DiscountCampaignModel.builder().build();
        }

        if (a.getId() != 0)
            discountCampaignModel.setId(a.getId());


        Date startDate = Date.from(a.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(a.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

        discountCampaignModel.setName(a.getName());
        discountCampaignModel.setDescription(a.getDescription());
        discountCampaignModel.setActive(true);
        discountCampaignModel.setStartDate(startDate);
        discountCampaignModel.setEndDate(endDate);
        discountCampaignModel.setListDiscountDetail(discountDetailModelList);

        for (DiscountDetailModel discountDetailModel : discountDetailModelList) {
            discountDetailModel.setDiscountCampaignModel(discountCampaignModel);
        }

        discountCampaignModel = discountCampaignRepository.save(discountCampaignModel);
        List<DiscountDetailModel> newDiscountDetailModelList = discountDetailRepository.saveAll(discountDetailModelList);
        List<ProductModel> productModelList = new ArrayList<>();
        for (DiscountDetailModel x : newDiscountDetailModelList) {
            for (ProductModel productModel : x.getListProduct()) {
                productModel.setDiscountDetailModel(x);
                productModelList.add(productModel);
            }
        }
        productRepository.saveAll(productModelList);
        return discountCampaignModel.getName();
    }

    @Override
    public String delete(long id) {
        DiscountCampaignModel discountCampaignModel = discountCampaignRepository.findById(id).orElseThrow(() -> new CustomException("Not found"));
        discountCampaignModel.setActive(!discountCampaignModel.isActive());
        for (DiscountDetailModel discountDetailModel : discountCampaignModel.getListDiscountDetail()) {
            for (ProductModel productModel : discountDetailModel.getListProduct()) {
                productModel.setDiscountDetailModel(null);
                productRepository.save(productModel);
            }
        }
        discountCampaignRepository.save(discountCampaignModel);
        return discountCampaignModel.getName();
    }

    @Override
    public DiscountCampaignDTO getDiscountCampaignDTOById(long id) {
        return discountCampaignMapper.convertToDTO(discountCampaignRepository.findById(id).orElseThrow(() -> new CustomException("Not Found")));
    }

    @Override
    public List<DiscountCampaignDTO> getAllDiscountCampaign() {
        return discountCampaignRepository.findAll().stream().map(discountCampaignMapper::convertToDTO).toList();
    }
}
