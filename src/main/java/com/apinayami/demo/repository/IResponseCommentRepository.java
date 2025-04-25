package com.apinayami.demo.repository;

import com.apinayami.demo.model.ResponseCommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IResponseCommentRepository extends JpaRepository<ResponseCommentModel, Long> {
}
