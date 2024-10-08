package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public interface ProductService
{
     ProductDTO addProductToCategory(Product product, Long categoryId);
     ProductResponse getProducts(int pageIndex, int pageSize, String sortBy, String sortOrder);
     ProductResponse getProductsByCategory(Long categoryId, int pageNumber, int pageLength,
                                           String sortBy, String sortOrder);
     ProductResponse getProductByKeyword(String keyword,int pageNumber, int pageLength,
                                         String sortBy, String sortOrder);

     ProductDTO updateProduct(Product product);



}