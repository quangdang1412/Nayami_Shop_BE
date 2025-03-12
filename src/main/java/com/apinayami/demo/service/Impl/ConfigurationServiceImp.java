package com.apinayami.demo.service.Impl;

import com.apinayami.demo.model.ConfigurationModel;
import com.apinayami.demo.repository.IConfigurationRepository;
import com.apinayami.demo.service.IConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImp implements IConfigurationService {

    private final IConfigurationRepository configurationRepository;

    @Override
    public String create(ConfigurationModel a) {
        configurationRepository.save(a);
        return a.getId().toString();
    }

    @Override
    public String update(ConfigurationModel a) {
        return null;
    }

    @Override
    public String delete(ConfigurationModel a) {
        return null;
    }
}
