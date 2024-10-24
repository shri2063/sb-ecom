package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private Integer roleId;
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private AppRole roleName;


    public Role(AppRole roleName) {
        this.roleName = roleName;
    }





}
