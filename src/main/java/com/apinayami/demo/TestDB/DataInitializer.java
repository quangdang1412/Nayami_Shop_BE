package com.apinayami.demo.TestDB;

import com.apinayami.demo.model.AddressModel;
import com.apinayami.demo.model.CustomerModel;
import com.apinayami.demo.model.ShippingModel;
import com.apinayami.demo.model.TestModel;
import com.apinayami.demo.repository.IAddressRepository;
import com.apinayami.demo.repository.ICustomerRepository;
import com.apinayami.demo.repository.IShippingRepository;
import com.apinayami.demo.util.Enum.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

//@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ICustomerRepository customerRepository;
    private final IAddressRepository addressRepository;
    private final IShippingRepository shippingRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
// 🧑 Tạo khách hàng
            CustomerModel customer = CustomerModel.builder()
                    .userName("nguyenvana")
                    .password("password123")
                    .type(Role.CUSTOMER)
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

            System.out.println("✅ Dữ liệu mẫu đã được tạo!");
        };
    }
}
