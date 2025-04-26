package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.response.ResponseCommentDTO;
import com.apinayami.demo.model.ResponseCommentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResponseCommentMapper {
    ResponseCommentMapper INSTANCE = Mappers.getMapper(ResponseCommentMapper.class);

    ResponseCommentModel toResponseModel(ResponseCommentDTO responseCommentDTO);

//    @Mapping(source = "staffModel", target = "staff")
    @Mapping(source = "commentModel", target = "comment")
    ResponseCommentDTO toResponseDTO(ResponseCommentModel responseCommentModel);
}
