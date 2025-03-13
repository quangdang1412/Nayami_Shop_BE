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
    public String create(BrandModel a) {
        try {
            log.info("Saving brand: {}", a.getBrandName());
            brandRepository.save(a);
            return "Thêm thành công " + a.getBrandName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Brand has been used");
        }
    }

    @Override
    public String update(BrandModel a) {
        try {
            log.info("Updating brand: {}", a.getBrandName());
            brandRepository.save(a);
            return "Cập nhật thành công " + a.getBrandName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return "Lỗi khi cập nhật " + a.getBrandName();
        }
    }

    @Override
    public String delete(BrandModel a) {
        try {
            log.info("Deleting brand: {}", a.getBrandName());
            brandRepository.delete(a);
            return "Xóa thành công " + a.getBrandName();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return "Lỗi khi xóa " + a.getBrandName();
        }
    }

}
