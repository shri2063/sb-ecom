package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService
{



    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Pageable pageRequest = PageRequest.of(pageNumber,pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);
        List<Category> categories =categoryPage.getContent();
        List<CategoryDTO> categoryDTOS = categories
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;

    }


    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        Optional<Category> categoryNameOptional = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(categoryNameOptional.isPresent()) {
            throw new APIException("Category Name Already Exists");
        }
        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(newCategory);

        return modelMapper.map(savedCategory, CategoryDTO.class);


    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
             categoryRepository.delete(category);
             return modelMapper.map(category, CategoryDTO.class);
        }
        else
        {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found");
        }



    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryDTO.getCategoryId());
        Optional<Category> nameOptional = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());

        if (categoryOptional.isEmpty())
        {
            throw new ResourceNotFoundException("Category", "categoryId", categoryDTO.getCategoryId());

        }
        else if(nameOptional.isPresent())
        {
            throw new APIException("Category name already exist");
        }
        else
        {
            Category updateCategory = categoryOptional.get();
            updateCategory.setCategoryName(categoryDTO.getCategoryName());
            Category updatedCategory = categoryRepository.save(updateCategory);
            return modelMapper.map(updatedCategory, CategoryDTO.class);


        }
    }
}
