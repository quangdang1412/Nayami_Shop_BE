package com.apinayami.demo.repository;

import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.model.DiscountDetailModel;
import com.apinayami.demo.model.ProductModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<ProductModel> hasSearchQuery(String searchQuery) {
        return (root, query, cb) -> {
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("productName")), "%" + searchQuery.toLowerCase() + "%");
        };
    }

    public static Specification<ProductModel> hasBrands(List<String> brands) {
        return (root, query, cb) -> {
            if (brands == null || brands.isEmpty()) return null;
            Join<ProductModel, BrandModel> brandJoin = root.join("brandModel");
            return brandJoin.get("brandName").in(brands);
        };
    }

    public static Specification<ProductModel> hasCategories(List<String> categories) {
        return (root, query, cb) -> {
            if (categories == null || categories.isEmpty()) return null;
            Join<ProductModel, CategoryModel> categoryJoin = root.join("categoryModel");
            return categoryJoin.get("categoryName").in(categories);
        };
    }

    public static Specification<ProductModel> hasMinRating(List<Integer> rating) {
        return (root, query, cb) -> {
            if (rating == null || rating.isEmpty()) return null;
            return root.get("ratingAvg").in(rating);
        };
    }

    public static Specification<ProductModel> hasDiscount(List<Integer> discount) {
        return (root, query, cb) -> {
            if (discount == null || discount.isEmpty()) return null;
            Join<ProductModel, DiscountDetailModel> discountJoin = root.join("discountDetailModel");
            Expression<Integer> discountPercentage = discountJoin.get("percentage");

            List<Predicate> predicates = new ArrayList<>();

            for (Integer code : discount) {
                switch (code) {
                    case 1 -> predicates.add(cb.between(discountPercentage, 0, 5));
                    case 2 -> predicates.add(cb.between(discountPercentage, 5, 10));
                    case 3 -> predicates.add(cb.between(discountPercentage, 10, 15));
                    case 4 -> predicates.add(cb.between(discountPercentage, 15, 25));
                    case 5 -> predicates.add(cb.greaterThanOrEqualTo(discountPercentage, 100));
                    default -> throw new IllegalArgumentException("Mã giảm giá không hợp lệ: " + code);
                }
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ProductModel> filterProducts(List<String> brands, List<String> categories, List<Integer> rating, List<Integer> discount, String searchQuery) {
        return Specification.where(hasSearchQuery(searchQuery))
                .and(hasBrands(brands))
                .and(hasCategories(categories))
                .and(hasMinRating(rating))
                .and(hasDiscount(discount));
    }
}

