package com.apinayami.demo.dto.response;

import com.apinayami.demo.dto.request.CommentDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.CommentModel;
import com.apinayami.demo.model.UserModel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCommentDTO implements Serializable {
    private long id;
    private String description;
//    private UserDTO staff;
    private CommentDTO comment;
}
