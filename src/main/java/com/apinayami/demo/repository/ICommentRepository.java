package com.apinayami.demo.repository;

import com.apinayami.demo.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findByProductModel_Id(long productId);
}
