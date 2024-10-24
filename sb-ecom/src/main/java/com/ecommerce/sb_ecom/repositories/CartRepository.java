package com.ecommerce.sb_ecom.repositories;

import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.model.CartLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
