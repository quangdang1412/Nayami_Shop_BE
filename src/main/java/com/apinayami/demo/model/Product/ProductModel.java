//package com.apinayami.demo.model.Product;
//
//import com.apinayami.demo.model.BrandModel;
//import com.apinayami.demo.model.CategoryModel;
//import com.apinayami.demo.model.CommentModel;
//import com.apinayami.demo.model.ImageModel;
//import com.apinayami.demo.util.Enum.EProductStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.experimental.SuperBuilder;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.util.Date;
//import java.util.List;
//
//@Getter
//@Setter
//@MappedSuperclass
//@SuperBuilder
//@NoArgsConstructor
//@AllArgsConstructor
//public abstract class ProductModel {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    protected Long id;
//
//    @Column(name = "created_at", updatable = false)
//    @CreationTimestamp
//    protected Date createdAt;
//
//    @Column(name = "updated_at")
//    @UpdateTimestamp
//    protected Date updatedAt;
//    protected String description;
//    protected String productName;
//
//    @ManyToOne
//    @JoinColumn(name = "brand_id")
//    protected BrandModel brandModel;
//
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    protected CategoryModel categoryModel;
//
//    protected boolean displayStatus;
//    protected EProductStatus productStatus;
//    protected Integer quantity;
//    protected Integer ratingAvg;
//    protected Double orginalPrice;
//    protected Double unitPrice;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productModel", cascade = CascadeType.ALL)
//    protected List<ImageModel> listImage;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commentModel", cascade = CascadeType.ALL)
//    protected List<CommentModel> listComment;
//}
//
