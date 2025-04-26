package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.response.ResponseCommentDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.ResponseCommentMapper;
import com.apinayami.demo.model.CommentModel;
import com.apinayami.demo.model.ResponseCommentModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICommentRepository;
import com.apinayami.demo.repository.IResponseCommentRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.IResponseCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseCommentService implements IResponseCommentService {
    private final IResponseCommentRepository responseCommentRepository;
    private final IUserRepository userRepository;
    private final ICommentRepository commentRepository;

    private final ResponseCommentMapper responseCommentMapper;

    @Override
    public String createResponseComment(String responseComment, String email, long commentId) {
        try{
            UserModel user = userRepository.findByEmail(email);
            CommentModel comment = commentRepository.findById(commentId).isPresent() ? commentRepository.findById(commentId).get() : null;

            ResponseCommentModel responseCommentModel = ResponseCommentModel.builder()
                    .description(responseComment)
                    .commentModel(comment)
                    .staffModel(user)
                    .build();

            responseCommentRepository.save(responseCommentModel);
            return "Khởi tạo thành công";
        }
        catch(Exception e){
            throw new CustomException("Lỗi tạo phản hồi");
        }
    }

    @Override
    public List<ResponseCommentDTO> getResponseCommentsByProductId(long productId) {
        try{
            List<ResponseCommentModel> responseCommentModels = responseCommentRepository.findAll().stream().filter(res -> res.getCommentModel().getProductModel().getId() == productId ).toList();
            return responseCommentModels.stream().map(responseCommentMapper::toResponseDTO).collect(Collectors.toList());
        }
        catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }
}
