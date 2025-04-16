package com.apinayami.demo.service.Impl;

import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.model.CustomUserDetail;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.util.Enum.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailCustomService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByEmail(email);

        if(userModel == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userModel.getType().name());
        return new CustomUserDetail(
                userModel.getEmail(),
                userModel.getPassword(),
                Collections.singletonList(authority),
                userModel.getUsername()
        );
    }
}
