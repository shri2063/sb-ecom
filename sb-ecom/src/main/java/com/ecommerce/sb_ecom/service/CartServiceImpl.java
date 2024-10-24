package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService
{


    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity, Long userId) {
        return null;
    }

    @Override
    public CartDTO updateProductQuantity(Long productId, String operation, Long userId) {
        return null;
    }

    @Override
    public String deleteProductFromCart(String cartId, String productId, Long userId) {
        return "";
    }



    @Override
    public List<CartDTO> getAllCarts() {
        return List.of();
    }

    @Override
    public CartDTO getUserCart(Long userID) {
        return null;
    }
}
