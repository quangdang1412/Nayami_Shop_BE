package com.apinayami.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentDTO implements Serializable {
    long id;
    String description;
    int rating;
    LocalDateTime createdAt;
    String userName;
    String userEmail;
    long productId;
    boolean active;
}
