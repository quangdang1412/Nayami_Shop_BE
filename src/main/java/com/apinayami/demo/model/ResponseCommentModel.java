package com.apinayami.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResponseCommentModel extends AbstractEntity<Long> {
    private String description;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private UserModel staffModel;
    @OneToOne
    @JoinColumn(name = "comment_id")
    private CommentModel commentModel;

}
