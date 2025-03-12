package com.apinayami.demo.service.Impl;

import com.apinayami.demo.model.OtherConfigurationModel;
import com.apinayami.demo.repository.IOtherConfigurationRepository;
import com.apinayami.demo.service.IOtherConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtherConfigurationServiceImpl implements IOtherConfigurationService {

    private final IOtherConfigurationRepository otherConfigurationRepository;

    @Override
    public String create(OtherConfigurationModel a) {
        otherConfigurationRepository.save(a);
        return a.getName();
    }

    @Override
    public String update(OtherConfigurationModel a) {
        otherConfigurationRepository.save(a);
        return a.getName();
    }

    @Override
    public String delete(OtherConfigurationModel a) {
        return null;
    }
}
