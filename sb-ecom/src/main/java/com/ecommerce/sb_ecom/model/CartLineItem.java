package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartLineItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @Column(name = "cart_id")
    private Cart cart;

    @OneToOne
    @Column(name="product_Id")
    private Product product;

    private int quantity;
}
