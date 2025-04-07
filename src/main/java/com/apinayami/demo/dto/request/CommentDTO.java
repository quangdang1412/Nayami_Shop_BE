package com.apinayami.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentDTO implements Serializable {
    long id;
    String description;
    int rating;
    String userEmail;
    long productId;
}
