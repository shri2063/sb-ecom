package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.controller.CategoryController;
import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.NoResourceException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
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

        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new NoResourceException("No categories found");
        }
        return categories;

    }

    @Override
    public void createCategory(Category category) {
        long len = (long) getAllCategories().size();
        Optional<Category> categoryNameOptional = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryNameOptional.isPresent()) {
            throw new APIException("Category Name Already Exists");
        }
        category.setCategoryId(len + 1);
        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            categoryRepository.delete(category);
        }
        else
        {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found");
        }


        return "removed category Id: " + categoryId + " deleted.";
    }

    @Override
    public Category updateCategory(Category category) {
        Optional<Category> categoryOptional = categoryRepository.findById(category.getCategoryId());

        if (categoryOptional.isPresent()) {
            Category updateCategory = categoryOptional.get();
            //Optional<Category> naeOptional = categoryRepository.findAll(x -> x.);
            updateCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(updateCategory);
        }
        else
        {
            throw new ResourceNotFoundException("Category", "categoryId", category.getCategoryId());
        }

        return categoryRepository.findById(category.getCategoryId()).get();
    }
}
