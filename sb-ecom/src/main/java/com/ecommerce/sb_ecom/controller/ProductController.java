package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.config.AppConstants;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ProductController
{

    @Autowired
    ProductService productService;
    @PostMapping("/api/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid  @PathVariable("categoryId") Long categoryId, @RequestBody ProductDTO productDTO)
    {
        return new ResponseEntity<>(productService.addProductToCategory(productDTO, categoryId), HttpStatus.CREATED);
    }
    @GetMapping ("/api/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageIndex",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false) int pageIndex,
            @RequestParam(name = "pageLength",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false) int pageLength,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder
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
        ProductResponse response = productService.getProductByKeyword(keyword,pageNumber,pageLength,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @PathVariable Long productId, ProductDTO productDTO)
    {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

}
