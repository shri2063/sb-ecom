package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.repositories.CartLineItemRepository;
import com.ecommerce.sb_ecom.repositories.CartRepository;
import com.ecommerce.sb_ecom.service.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;

@RestController
@RequestMapping("api/carts")
public class ShoppingCartController
{
    @Autowired
    CartLineItemRepository cartLineItemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartServiceImpl cartService;
    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @RequestParam Long productId,
            @RequestParam String quantity,
            Authentication authentication)
    {

    }

    @PutMapping("/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantity(
            @RequestParam Long productId,
            @RequestParam String operation,
            Authentication authentication)
    {


    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<CartDTO> deleteProductFromCart(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            Authentication authentication)
    {

    }

    @GetMapping("")
    public ResponseEntity<List<CartDTO>> getAllCarts()
    {

    }

    @GetMapping("/users/cart")
    public ResponseEntity<CartDTO> getUserCart(Authentication authentication)
    {

    }
}
