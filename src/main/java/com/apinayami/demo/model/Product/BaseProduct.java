package com.apinayami.demo.model.Product;

import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.model.CommentModel;
import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.util.Enum.EProductStatus;
import jakarta.annotation.Nullable;
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

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
    protected String description;
    protected String productName;

    protected boolean displayStatus;
    protected EProductStatus productStatus;
    protected Integer quantity;
    protected Integer ratingAvg;
    protected Double orginalPrice;
    protected Double unitPrice;


    //references
    @ManyToOne
    @JoinColumn(name = "brand_id")
    protected BrandModel brandModel;

    @ManyToOne
    @JoinColumn(name = "category_id")
    protected CategoryModel categoryModel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    @Nullable // test
    protected List<ImageModel> listImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
    @Nullable // test
    protected List<CommentModel> listComment;

}

