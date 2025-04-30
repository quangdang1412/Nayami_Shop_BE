package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LineItemModel extends AbstractEntity<Long> {
    private Integer quantity;
    private Double unitPrice;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel productModel;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private BillModel billModel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lineItemModel", cascade = CascadeType.ALL)
    private List<SerialProductModel> listSerialProduct;
}
