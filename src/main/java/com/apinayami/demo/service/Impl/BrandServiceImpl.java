package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.BrandMapper;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.repository.IBrandRepository;
import com.apinayami.demo.service.IBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl implements IBrandService {
    private final IBrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public List<BrandDTO> getAllBrand() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public BrandModel findBrandById(Long id) {
        return brandRepository.findBrandById(id);
    }

    @Override
    public String create(BrandDTO a) {
        try {
            log.info("Saving brand: {}", a.getName());
            if (brandRepository.existsByBrandName(a.getName())) {
                throw new CustomException("Brand already exists");

            }
            BrandModel brandModel = new BrandModel();
            brandModel.setBrandName(a.getName());

            brandRepository.save(brandModel);
            return "Thêm thành công " + a.getName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public String update(BrandDTO a) {
        try {
            log.info("Updating brand: {}", a.getName());
            if (brandRepository.existsByBrandName(a.getName())) {
                throw new CustomException("Brand already exists");

            }
            BrandModel brandModel = brandRepository.findBrandById(a.getId());
            if (brandModel == null) {
                throw new CustomException("Brand not found");
            }
            brandModel.setBrandName(a.getName());
            brandModel.setActive(a.isActive());
            brandRepository.save(brandModel);
            return "Cập nhật thành công " + a.getName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public String delete(Long a) {
        try {
            BrandModel brandModel = brandRepository.findBrandById(a);
            if (brandModel == null) {
                throw new CustomException("Brand not found");
            }
            log.info("Deleting brand: {}", brandModel.getBrandName());
            brandModel.setActive(false);
            brandRepository.save(brandModel);
            return "Thay đổi trạng thái thành công " + brandModel.getBrandName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
    }

}
