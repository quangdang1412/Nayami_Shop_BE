package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.CommentDTO;

import java.util.List;

public interface ICommentService{
    List<CommentDTO> getAllComments();
    List<CommentDTO> getCommentByProductId(long id);
    CommentDTO getCommentById(long id);
    String create(CommentDTO comment);
    List<CommentDTO> getCommentByUserEmail(String email);
    String updateStatus(long id);
}
