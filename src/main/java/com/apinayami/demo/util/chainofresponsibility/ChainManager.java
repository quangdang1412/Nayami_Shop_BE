package com.apinayami.demo.util.chainofresponsibility;

import com.apinayami.demo.dto.request.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChainManager {
    private final ProductExistValidator productExistValidator;
    private final UserExistValidator userExistValidator;

    public void validate(CommentDTO commentDTO) {
        productExistValidator.setNextValidator(userExistValidator);

        productExistValidator.validate(commentDTO);
    }
}
