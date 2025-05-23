package com.apinayami.demo.util.chainofresponsibility;

import com.apinayami.demo.dto.request.CommentDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistValidator extends BaseValidator{
    private final IUserRepository userRepository;
    @Override
    public void handle(CommentDTO commentDTO) {
        if(!userRepository.existsByEmail(commentDTO.getUserEmail())){
            throw new CustomException("User does not exist");
        }
    }
}
