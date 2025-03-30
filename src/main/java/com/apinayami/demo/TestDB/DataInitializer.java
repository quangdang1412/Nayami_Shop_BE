package com.apinayami.demo.TestDB;

import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.*;
import com.apinayami.demo.util.Enum.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final IUserRepository userRepository;
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;
    private final IBrandRepository brandRepository;
    private final IOtherConfigurationRepository otherConfigurationRepository;
    private final IConfigurationRepository configurationRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
//            // Kiểm tra xem đã có dữ liệu hay chưa
//            if (brandRepository.count() > 0 || categoryRepository.count() > 0 || productRepository.count() > 0) {
//                System.out.println("⚠️ Dữ liệu đã tồn tại, không cần khởi tạo lại!");
//                return;
//            }

//            // ✅ Tạo Brand
//            BrandModel brandApple = BrandModel.builder().brandName("Apple").build();
//            BrandModel brandSamsung = BrandModel.builder().brandName("Samsung").build();
//            brandRepository.saveAll(Arrays.asList(brandApple, brandSamsung));
//
//            // ✅ Tạo Category
            CategoryModel categoryPhone = CategoryModel.builder().categoryName("Phone").build();
            CategoryModel categoryLaptop = CategoryModel.builder().categoryName("Laptop").build();
            categoryRepository.saveAll(Arrays.asList(categoryPhone, categoryLaptop));
            // ✅ Tạo Promotion
//            CategoryModel categoryPhone = CategoryModel.builder().categoryName("Phone").build();
//            CategoryModel categoryLaptop = CategoryModel.builder().categoryName("Laptop").build();
//            categoryRepository.saveAll(Arrays.asList(categoryPhone, categoryLaptop));

//            // ✅ Tạo Configuration cho Category
//            ConfigurationModel configurationPhone = ConfigurationModel.builder()
//                    .categoryModel(categoryPhone)
//                    .build();
//            configurationRepository.save(configurationPhone);
            // ✅ Tạo Admin
//            UserModel userModel = UserModel.builder()
//                    .userName("user")
//                    .email("demotranbao111@gmail.com")
//                    .password("baooa4477")
//                    .type(Role.ADMIN)
//                    .active(true)
//                    .phoneNumber(null)
//                    .build();
//            String hashedPassword = passwordEncoder.encode(userModel.getPassword());
//            userModel.setPassword(hashedPassword);
//            userModel.setActive(true);
//            userRepository.save(userModel);
//
//            // ✅ Tạo Product
//            ProductModel productSamsung = ProductModel.builder()
//                    .productName("Samsung Galaxy S25 Ultra 12GB 256GB")
//                    .description("Samsung Galaxy S25")
//                    .displayStatus(true)
//                    .productStatus(EProductStatus.ON_SELL)
//                    .quantity(100)
//                    .categoryModel(categoryPhone)
//                    .originalPrice(30990000.0)
//                    .unitPrice(33990000.0)
//                    .brandModel(brandSamsung)
//                    .configurationModel(configurationPhone)
//                    .build();
//
//            // ✅ Tạo Other Configuration (RAM & ROM)
//            OtherConfiguration ramPhone = OtherConfiguration.builder().name("RAM").value("4GB").build();
//            OtherConfiguration romPhone = OtherConfiguration.builder().name("ROM").value("128GB").build();
//
//            // ✅ Gán Other Configuration vào ConfigurationModel
//            ramPhone.setConfigurationModel(configurationPhone);
//            romPhone.setConfigurationModel(configurationPhone);
//            otherConfigurationRepository.saveAll(Arrays.asList(ramPhone, romPhone));
//
//            // ✅ Cập nhật danh sách Other Configuration cho ConfigurationModel
//            configurationPhone.setOtherConfigurationList(Arrays.asList(ramPhone, romPhone));
//
//            // ✅ Lưu Product vào Database
//            productRepository.save(productSamsung);

            System.out.println("✅ Dữ liệu mẫu đã được tạo!");
        };
    }
}
