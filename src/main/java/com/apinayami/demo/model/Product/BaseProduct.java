package com.apinayami.demo.model.Product;

import com.apinayami.demo.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
    private String productName;
    private String description;
    private boolean displayStatus;
    private Integer quantity;
    private Integer ratingAvg;
    private Double orginalPrice;
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandModel brandModel;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel categoryModel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    private List<ImageModel> listImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    private List<CommentModel> listComment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    private List<LineItemModel> listLineItem;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
//    private List<CommentModel> listComment;
}
