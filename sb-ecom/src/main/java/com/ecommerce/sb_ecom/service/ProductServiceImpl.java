package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import com.ecommerce.sb_ecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductServiceImpl implements ProductService
{
    @Autowired

    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    public ProductDTO addProductToCategory(Product product, Long categoryId)
    {


        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        product.setCategory(category);
        Product addedProduct = productRepository.save(product);
        return modelMapper.map(addedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getProducts(int pageIndex, int pageSize, String sortBy, String sortOrder) {
        Sort sort;
        if(sortOrder.equals("asc"))
        {
         sort = Sort.by(sortBy).ascending();
        }
        else
        {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageRequest = PageRequest.of(pageIndex, pageSize, sort);
        Page<Product> page =  productRepository.findAll(pageRequest);
        List<Product> productList = page.getContent();
        List<ProductDTO> productDTOList = productList
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNum(page.getNumber());
        productResponse.setPageSize(page.getSize());
        productResponse.setTotal(page.getTotalElements());
        productResponse.setLastPage(page.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, int pageNumber, int pageIndex,
                                                 String sortBy, String sortOrder) {

        Sort sort;
        if(sortOrder.equals("asc"))
        {
            sort = Sort.by(sortBy).ascending();
        }
        else
        {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageIndex,sort);

        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
                ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOList);
        productResponse.setPageNum(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotal(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;

    }

    @Override
    public ProductResponse getProductByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort;
        if(sortOrder.equals("asc"))
        {
            sort = Sort.by(sortBy).ascending();
        }
        else
        {
            sort = Sort.by(sortBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> productPage = productRepository.findByKeyword(keyword,pageable);

        ProductResponse productResponse = new ProductResponse();
        List<ProductDTO> productDTOList = productPage.getContent()
                .stream()
                        .map(product -> modelMapper.map(product, ProductDTO.class))
                                .toList();
        productResponse.setContent(productDTOList);
        productResponse.setPageNum(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotal(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(Product product) {
        Product existProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", product.getId()));
        Product updated = productRepository.save(existProduct);
        return modelMapper.map(updated, ProductDTO.class);
    }


}
