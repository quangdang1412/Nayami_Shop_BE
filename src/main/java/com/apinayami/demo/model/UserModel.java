package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phoneNumber") })
public class UserModel extends AbstractEntity<Long> {
    protected String userName;
    protected String password;
    @Enumerated(EnumType.STRING)
    protected Role type;
    protected String email;
    protected boolean active;
    protected String phoneNumber;

    //reference
        //staff
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "staffModel", cascade = CascadeType.ALL)
    private Set<ResponseCommentModel> listResponseComments;

        //customer
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<AddressModel> listAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<CouponModel> listCoupon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<CartItemModel> listCartItem;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<BillModel> listBill;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<CommentModel> listComment;
}
