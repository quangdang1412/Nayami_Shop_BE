package com.apinayami.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResponseCommentModel extends AbstractEntity<Long> {
    private String description;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffModel staffModel;
    @OneToOne
    @JoinColumn(name = "comment_id")
    private CommentModel commentModel;

}
