package com.apinayami.demo.util.chainofresponsibility;

import com.apinayami.demo.dto.request.CommentDTO;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
public abstract class BaseValidator {
    private BaseValidator nextValidator;

    public void validate(CommentDTO commentDTO) {
        handle(commentDTO);
        if(nextValidator != null) {
            nextValidator.validate(commentDTO);
        }
    }

    public abstract void handle(CommentDTO commentDTO);
}
