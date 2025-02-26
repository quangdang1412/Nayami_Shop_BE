package com.apinayami.demo.TestDB;

import com.apinayami.demo.model.*;
import com.apinayami.demo.model.Product.BaseProduct;
import com.apinayami.demo.model.Product.LaptopModel;
import com.apinayami.demo.model.Product.PhoneModel;
import com.apinayami.demo.repository.*;
import com.apinayami.demo.util.Enum.EProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ShippingRepository shippingRepository;
    private final CategoryRepository categoryRepository;
    private final PhoneRepository phoneRepository;
    private final CommentRepositroy commentRepositroy;


    @Bean
    public CommandLineRunner initData(LaptopRepository laptopRepository) {
        return args -> {
// 🧑 Tạo khách hàng
            CustomerModel customer = CustomerModel.builder()
                    .userName("nguyenvana")
                    .password("password123")
                    .email("nguyenvana@example.com")
                    .active(true)
                    .phoneNumber("0123456789")
                    .build();
            customer = customerRepository.save(customer);
//
// 🏠 Tạo địa chỉ cho khách hàng
            AddressModel address = AddressModel.builder()
                    .customerModel(customer)
                    .shippingContactNumber("0987654321")
                    .detail("123 Đường ABC")
                    .province("Hà Nội")
                    .district("Đống Đa")
                    .village("Phường XYZ")
                    .active(true)
                    .build();
            address = addressRepository.save(address);

            // 📦 Tạo đơn hàng vận chuyển
            ShippingModel shipping = ShippingModel.builder()
                    .shippingAddress(address)
                    .shippingFee(50_000)
                    .build();
            shippingRepository.save(shipping);
            TestModel testModel = TestModel.builder().id("hehehe").name("hahaha").build();


            //test data category
            CategoryModel categoryPhone = CategoryModel.builder()
                    .categoryName("Phone")
                    .build();
            categoryPhone = categoryRepository.save(categoryPhone);
            CategoryModel categoryLaptop = CategoryModel.builder()
                    .categoryName("Laptop")
                    .build();
            categoryLaptop = categoryRepository.save(categoryLaptop);
            //test data phone
            //Phone have a foreign key reference to category
            LaptopModel laptop = null;
            try {
                laptop = LaptopModel.builder()
                        .productName("Iphone 13")
                        .productStatus(EProductStatus.ON_SELL)
                        .description("This is iphone 13")
                        .displayStatus(false)
                        .ratingAvg(5)
                        .quantity(4)
                        .orginalPrice(100d)
                        .unitPrice(150d).categoryModel(categoryLaptop).build();
                ;
                laptop = laptopRepository.save(laptop);
                System.out.println("✅ Dữ liệu mẫu cho laptop đã được tạo!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("✅ Lỗi lúc khởi tạo dữ liệu cho phone");
            }
            //test data laptop
            PhoneModel phone = null;
            try {
                phone = PhoneModel.builder()
                        .productName("Iphone 13")
                        .productStatus(EProductStatus.ON_SELL)
                        .description("This is iphone 13")
                        .displayStatus(false)
                        .ratingAvg(5)
                        .quantity(4)
                        .color("blue")
                        .orginalPrice(100d)
                        .unitPrice(150d).categoryModel(categoryPhone).build();
                ;
                phone = phoneRepository.save(phone);
                System.out.println("✅ Dữ liệu mẫu cho phone đã được tạo!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("✅ Lỗi lúc khởi tạo dữ liệu cho phone");
            }


            //test data comment
            CommentModel commnentPhone = null;
            try {
                commnentPhone = CommentModel.builder()
                        .customerModel(customer)
                        .productModel(phone)
                        .rating(5)
                        .description("This phone is great")
                        .build();
                commnentPhone = commentRepositroy.save(commnentPhone);
                System.out.println("✅ Dữ liệu mẫu cho comment phone đã được tạo!");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("✅ Lỗi lúc khởi tạo dữ liệu cho comment phone");
            }
                //comment for laptop
            CommentModel commentLaptop = null;
            try {
                commentLaptop = CommentModel.builder()
                        .customerModel(customer)
                        .productModel(laptop)
                        .rating(5)
                        .description("This phone is great")
                        .build();
                commentLaptop = commentRepositroy.save(commentLaptop);
                System.out.println("✅ Dữ liệu mẫu cho comment laptop đã được tạo!");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("✅ Lỗi lúc khởi tạo dữ liệu cho comment laptop");
            }



            System.out.println("✅ Dữ liệu mẫu đã được tạo!");
        };
    }
}
