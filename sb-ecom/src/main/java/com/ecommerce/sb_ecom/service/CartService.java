package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;

import java.util.List;

public interface CartService
{

    CartDTO addProductToCart(Long productId, Integer quantity, Long userId);
    CartDTO updateProductQuantity(Long productId, String operation, Long userId);
    String deleteProductFromCart(String cartId, String productId, Long userId);
    List<CartDTO> getAllCarts( );
    CartDTO getUserCart(Long userID);

}
