package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.UserRequestDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.model.StaffModel;
import com.apinayami.demo.service.IStaffService;
import com.apinayami.demo.util.Enum.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffServiceImpl implements IStaffService {
    private final IStaffRepository staffRepository;

    @Override
    public String create(UserRequestDTO a) {
        try {
            StaffModel staffModel = StaffModel.builder()
                    .userName(a.getUserName())
                    .password(a.getPassword())
                    .email(a.getEmail())
                    .phoneNumber(a.getPhoneNumber())
                    .active(true)
                    .type(Role.STAFF)
                    .build();
            staffRepository.save(staffModel);
            return a.getUserName();
        } catch (Exception e) {
            String error = e.getMessage();
            String property = error.substring(error.lastIndexOf(".") + 1, error.lastIndexOf("]"));
            log.info(error);
            throw new CustomException(property + " has been used");
        }
    }

    @Override
    public String update(UserRequestDTO a) {
        return null;
    }

    @Override
    public String delete(UserRequestDTO a) {
        return null;
    }
}
