package com.apinayami.demo.repository;

import com.apinayami.demo.model.CommentModel;
import com.apinayami.demo.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepositroy extends JpaRepository<CommentModel, Long> {
}
