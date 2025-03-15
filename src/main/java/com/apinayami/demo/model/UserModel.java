package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phoneNumber") })
public class UserModel extends AbstractEntity<Long> implements UserDetails {
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

    public Object orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(type.name()));
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
