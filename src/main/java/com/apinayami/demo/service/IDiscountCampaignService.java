package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.DiscountCampaignDTO;

import java.util.List;

public interface IDiscountCampaignService {
    String create(DiscountCampaignDTO a);

    String delete(long id);

    DiscountCampaignDTO getDiscountCampaignDTOById(long id);

    List<DiscountCampaignDTO> getAllDiscountCampaign();
}
