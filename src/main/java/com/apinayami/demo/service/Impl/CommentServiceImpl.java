package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.CommentDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.CategoryMapper;
import com.apinayami.demo.mapper.CommentMapper;
import com.apinayami.demo.model.CommentModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICommentRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.ICommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements ICommentService {
    private final ICommentRepository commentRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;
    private final CommentMapper commentMapper;
    @Override
    public List<CommentDTO> getAllComments() {
        List<CommentModel> commentModel = commentRepository.findAll();
        return commentModel.stream().map(commentMapper::ToCommentDTO).collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentByProductId(long id) {
        return commentRepository.findByProductModel_Id(id).stream().map(commentMapper::ToCommentDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(long id) {
        return CommentMapper.INSTANCE.ToCommentDTO(commentRepository.findById(id).isPresent() ? commentRepository.findById(id).get() : null);
    }

    @Override
    public String create(CommentDTO commentDTO) {
        try{
            UserModel userModel = userRepository.findByEmail(commentDTO.getUserEmail());
            ProductModel productModel = productRepository.findById(commentDTO.getProductId()).get();
            CommentModel commentModel = commentMapper.ToCommentModel(commentDTO);
            commentModel.setCustomerModel(userModel);
            commentModel.setProductModel(productModel);
            commentRepository.save(commentModel);

            List<CommentDTO> commentDTOS = getCommentByProductId(productModel.getId());
            int avgRate = commentDTOS.stream().mapToInt(CommentDTO::getRating).sum() / commentDTOS.size();
            System.out.println(avgRate);
            productModel.setRatingAvg(avgRate);
            productRepository.save(productModel);

            return "Thêm thành công " + commentModel.getDescription();
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Promotion has existed");
        }
    }

    @Override
    public List<CommentDTO> getCommentByUserEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        return commentRepository.findByProductModel_Id(user.getId()).stream().map(commentMapper::ToCommentDTO).collect(Collectors.toList());
    }
}
