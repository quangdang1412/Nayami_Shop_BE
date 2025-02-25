package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity<T extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected T id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
}
