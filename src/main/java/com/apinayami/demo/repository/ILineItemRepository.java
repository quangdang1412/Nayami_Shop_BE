package com.apinayami.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.apinayami.demo.model.LineItemModel;

public interface ILineItemRepository extends JpaRepository<LineItemModel, Long> {
    
}
