package com.apinayami.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    @NotNull(message = "ID không được để trống")
    @Positive(message = "ID phải là số dương")
    private Long id;

    @NotNull(message = "Product ID không được để trống")
    @Positive(message = "Product ID phải là số dương")
    private Long productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String productName;

    @Min(value = 0, message = "Phần trăm giảm giá không được nhỏ hơn 0")
    @Max(value = 100, message = "Phần trăm giảm giá không được vượt quá 100")
    private Integer percentDiscount;

    @NotNull(message = "Danh sách hình ảnh không được để trống")
    private List<@NotBlank(message = "Tên ảnh không được để trống") String> listImage;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Đơn giá không được để trống")
    @PositiveOrZero(message = "Đơn giá không được âm")
    private Double unitPrice;

    @NotNull(message = "Tổng giá không được để trống")
    @PositiveOrZero(message = "Tổng giá không được âm")
    private Double totalPrice;
}
