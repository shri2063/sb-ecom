package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import com.ecommerce.sb_ecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Value("123")
    private String path;


    @Override
    public ProductDTO addProductToCategory(ProductDTO productDTO, Long categoryId)
    {


        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        Optional<Product> productOpt = productRepository.findById(productDTO.getProductId());
        if(productOpt.isPresent())
        {
            throw new APIException("Product already exists");
        }
        Product product = modelMapper.map(productDTO, Product.class);
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
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->  new ResourceNotFoundException("category", "id", categoryId));

        //Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        Page<Product> productPage = productRepository.findByCategoryOrderByPrice(category, pageable);

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
        //Page<Product> productPage = productRepository.findByKeyword(keyword,pageable);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase(keyword, pageable);

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
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productDTO.getProductId()));
        Product updated = productRepository.save(existProduct);
        return modelMapper.map(updated, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product deleteProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
       productRepository.delete(deleteProduct);
        return modelMapper.map(deleteProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = uploadImage(path, image);
        productFromDb.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;
        File folder = new File(path);
        if (!folder.exists())
        {
            folder.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }


}
