package com.apinayami.demo.util.chainofresponsibility;

import com.apinayami.demo.dto.request.CommentDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductExistValidator extends BaseValidator{
    private final IProductRepository productRepository;
    @Override
    public void handle(CommentDTO commentDTO) {
        if(!productRepository.existsById(commentDTO.getProductId())){
            throw new CustomException("Product does not exist");
        }
    }
}
