package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.Role;
import jakarta.persistence.Column;
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
    @Column(nullable = false)
    protected String userName;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    protected String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    protected Role type;
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    protected String email;
    @Column
    protected boolean active;
    @Column(columnDefinition = "VARCHAR(10)")
    protected String phoneNumber;
}
