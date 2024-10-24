package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @Column(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartLineItem> cartLineItemsList;



}
