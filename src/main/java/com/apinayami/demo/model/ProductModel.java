package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel extends AbstractEntity<Long> {

    private String productName;
    private String description;
    private boolean displayStatus;
    private Integer quantity;
    private Integer ratingAvg;
    private Double originalPrice;
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandModel brandModel;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private DiscountDetailModel discountDetailModel;

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private ConfigurationModel configurationModel;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel categoryModel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    private List<ImageModel> listImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    private List<CommentModel> listComment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    private List<LineItemModel> listLineItem;


}
