package com.apinayami.demo.service;

import com.apinayami.demo.dto.response.ResponseCommentDTO;

import java.util.List;

public interface IResponseCommentService{
    String createResponseComment(String responseComment, String email, long commentId);
    List<ResponseCommentDTO> getResponseCommentsByProductId(long productId);

}
