package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ProductController
{

    @Autowired
    ProductService productService;
    @PostMapping("/api/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable("categoryId") Long categoryId, @RequestBody Product product)
    {
        return new ResponseEntity<>(productService.addProductToCategory(product, categoryId), HttpStatus.CREATED);
    }
    @GetMapping ("/api/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("pageLength") int pageLength,
            @RequestParam("sortby") String sortBy,
            @RequestParam("sortOrder") String sortOrder
    )
    {
        return ok(productService.getProducts(pageIndex,pageLength,sortBy,sortOrder));
    }
    @GetMapping("/api/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(
            @PathVariable("categoryId") long categoryId,
            @RequestParam("pageIndex") int pageNumber,
            @RequestParam("pageLength") int pageLength,
            @RequestParam("sortby") String sortBy,
            @RequestParam("sortOrder") String sortOrder
    )
    {
    return ok(productService.getProductsByCategory(categoryId,pageNumber,pageLength,sortBy,sortOrder));
    }

    @GetMapping("/api/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable("keyword") String keyword,
            @RequestParam("pageIndex") int pageNumber,
            @RequestParam("pageLength") int pageLength,
            @RequestParam("sortby") String sortBy,
            @RequestParam("sortOrder") String sortOrder
    )
    {
        return ok(productService.getProductByKeyword(keyword,pageNumber,pageLength,sortBy,sortOrder));
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(Product product)
    {
        return ok(productService.updateProduct(product));
    }

}