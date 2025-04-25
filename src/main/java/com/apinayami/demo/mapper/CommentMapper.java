package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.CommentDTO;
import com.apinayami.demo.model.CommentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommentModel ToCommentModel(CommentDTO commentDTO);

    @Mapping(source = "customerModel.email", target = "userEmail")
    @Mapping(source = "customerModel.username", target = "userName")
    @Mapping(source = "productModel.id", target = "productId")
    @Mapping(source = "active", target = "active")
    CommentDTO ToCommentDTO(CommentModel commentModel);

}
