package com.apinayami.demo.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.model.ProductModel;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mappings({
        // @Mapping(source = "", target = "unitPrice"),
    })
    CartItemModel toCartItemModel(CartItemDTO dto);
    





    @Mappings({
        @Mapping(source = "productModel.id", target = "productId"),
        @Mapping(source = "productModel.productName", target = "productName"),
        @Mapping(source = "productModel.unitPrice", target = "unitPrice"),
        @Mapping(source = "productModel.listImage", target = "listImage", qualifiedByName = "mapImagesToUrls"),
        @Mapping(source = "model", target = "totalPrice", qualifiedByName = "calculateTotalPrice"),
        @Mapping(source = "productModel", target = "percentDiscount", qualifiedByName = "extractDiscountPercentage")
    })  
    CartItemDTO toCartItemDTO(CartItemModel model);

    @Named("mapImagesToUrls")
    default List<String> mapImagesToUrls(List<ImageModel> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList(); 
        }
        return images.stream()
                     .map(ImageModel::getUrl) 
                     .collect(Collectors.toList());
    }
    @Named("calculateTotalPrice")
    default Double calculateTotalPrice(CartItemModel model) {
        if (model == null || model.getQuantity() == null || model.getProductModel() == null || model.getProductModel().getUnitPrice() == null) {
            return 0.0;
        }
        double unitPrice = model.getProductModel().getUnitPrice();

        if (model.getProductModel().getDiscountDetailModel() != null 
        && model.getProductModel().getDiscountDetailModel().getPercentage() != null) {
            
            double discountPercentage = model.getProductModel().getDiscountDetailModel().getPercentage();
            double discountAmountPerUnit = unitPrice * (discountPercentage / 100);
            unitPrice -= discountAmountPerUnit;             
        }
        double quantity = model.getQuantity();
        return unitPrice * quantity;
    }
    @Named("extractDiscountPercentage")
    default Double extractDiscountPercentage(ProductModel productModel) {
        if (productModel == null || productModel.getDiscountDetailModel() == null || productModel.getDiscountDetailModel().getPercentage() == null) {
            return 0.0;
        }
        return productModel.getDiscountDetailModel().getPercentage();
    }
}
