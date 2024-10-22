package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ManyToMany(mappedBy = "roles")
    private Set<ClientUser> users = new HashSet<ClientUser>();


}