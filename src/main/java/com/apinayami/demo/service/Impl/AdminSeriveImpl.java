//package com.apinayami.demo.service.Impl;
//
//import com.apinayami.demo.dto.request.UserRequestDTO;
//import com.apinayami.demo.exception.CustomException;
//import com.apinayami.demo.model.AdminModel;
//import com.apinayami.demo.service.IAdminService;
//import com.apinayami.demo.util.Enum.Role;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class AdminSeriveImpl implements IAdminService {
//    private final IAdminRepository adminRepository;
//
//    @Override
//    public String create(UserRequestDTO a) {
//        try {
//            AdminModel adminModel = AdminModel.builder()
//                    .userName(a.getUserName())
//                    .password(a.getPassword())
//                    .email(a.getEmail())
//                    .phoneNumber(a.getPhoneNumber())
//                    .active(true)
//                    .type(Role.ADMIN)
//                    .build();
//            adminRepository.save(adminModel);
//            return a.getUserName();
//        } catch (Exception e) {
//            String error = e.getMessage();
//            String property = error.substring(error.lastIndexOf(".") + 1, error.lastIndexOf("]"));
//            log.info(error);
//            throw new CustomException(property + " has been used");
//        }
//    }
//
//    @Override
//    public String update(UserRequestDTO a) {
//        return null;
//    }
//
//    @Override
//    public String delete(UserRequestDTO a) {
//        return null;
//    }
//}
