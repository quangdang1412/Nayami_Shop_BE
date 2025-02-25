package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserModel extends AbstractEntity<Long> {
    protected String userName;
    protected String password;
    @Enumerated(EnumType.STRING)
    protected Role type;
    protected String email;
    protected boolean active;
    protected String phoneNumber;
}
