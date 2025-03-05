package com.apinayami.demo.TestDB;

import com.apinayami.demo.model.*;
import com.apinayami.demo.repository.*;
import com.apinayami.demo.util.Enum.EProductStatus;
import com.apinayami.demo.util.Enum.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.print.attribute.standard.MediaSize;
import com.apinayami.demo.model.BrandModel;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ShippingRepository shippingRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final OtherConfigurationRepository otherConfigurationRepository;
    private final ConfigurationRepository configurationRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            //tao brand
            BrandModel brandApple = BrandModel.builder().brandName("Apple").build();
            BrandModel brandSamsung = BrandModel.builder().brandName("Samsung").build();
            brandRepository.save(brandApple);
            brandRepository.save(brandSamsung);
            //category
            CategoryModel categoryPhone = CategoryModel.builder().categoryName("Phone").build();
            CategoryModel categoryLaptop = CategoryModel.builder().categoryName("Laptop").build();
            categoryRepository.save(categoryPhone);
            categoryRepository.save(categoryLaptop);
            //configuration for category
                //other configuration for phone category
            ConfigurationModel configurationPhone = ConfigurationModel.builder().
                    categoryModel(categoryPhone).build();
            configurationRepository.save(configurationPhone);
            //Product
                //Add a new product
                //add default configuration for category
            ProductModel productSamsung = ProductModel.builder().
                    productName("Samsung Galaxy S25 Ultra 12GB 256GB").
                    description("Samsung Galaxy S25").
                    displayStatus(true).
                    productStatus(EProductStatus.ON_SELL).
                    quantity(100).
                    categoryModel(categoryPhone).
                    originalPrice(30990000.0).
                    unitPrice(33990000.0).
                    brandModel(brandSamsung).
                    configurationModel(configurationPhone).
                    build();

            OtherConfiguration ramPhone = OtherConfiguration.builder().
                    name("RAM").value("4GB").build();
            OtherConfiguration romPhone = OtherConfiguration.builder().
                    name("ROM").value("128GB").build();

            //get configuration of phone category
            ConfigurationModel configurationPhoneCategory = productSamsung.getConfigurationModel();
                //set new other configuration to configuration
            ramPhone.setConfigurationModel(configurationPhoneCategory);
            romPhone.setConfigurationModel(configurationPhoneCategory);
            otherConfigurationRepository.save(ramPhone);
            otherConfigurationRepository.save(romPhone);

            configurationPhoneCategory.setOtherConfigurationList(Arrays.asList(ramPhone, romPhone));
            productSamsung.setConfigurationModel(configurationPhone);
            productRepository.save(productSamsung);
            /** Quy trinh add phone
             * add configuration. Configuration thuoc ve mot category
             * Luc them phone se set duoc category cho phone
             * Phone chua co configuration: Get category tu phone -> get configuration tu category (vi config thuoc ve category)
             * Dau tien se tao other configuration -> set configuration cho otherconfiguration (khoa ngoai, trong otherconfiguration co khoa ngoai tham chieu toi configuration)
             * them other configuration truoc
             * sau do set configuration cho product
             * add product
             */




            System.out.println("✅ Dữ liệu mẫu đã được tạo!");
        };
    }
}
