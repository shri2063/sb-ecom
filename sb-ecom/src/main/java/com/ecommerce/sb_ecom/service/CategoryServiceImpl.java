package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.controller.CategoryController;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService
{


    private CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        long len = (long) categoryRepository.findAll().size();
        category.setCategoryId(len + 1);
        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
       Optional<Category> delCategoryOptional = categoryRepository.findById(categoryId);
       Category delCategory = delCategoryOptional
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"category does not exist"));
        categoryRepository.delete(delCategory);

        return "removed category Id: " + categoryId + " deleted.";
    }

    @Override
    public Category updateCategory(Category category) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(category.getCategoryId());
        Category existingCategory =existingCategoryOptional
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND,"category does not exist"));

        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);


    }
}
