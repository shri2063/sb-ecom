package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.controller.CategoryController;
import com.ecommerce.sb_ecom.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService
{

    private List<Category> categories = new ArrayList<>();

    public CategoryServiceImpl() {

    }

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        long len = (long) categories.size();
        category.setCategoryId(len + 1);
        categories.add(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found") );
        categories.remove(category);

        return "removed category Id: " + categoryId + " deleted.";
    }

    @Override
    public Category updateCategory(Category category) {
        Category oldcategory = categories
                .stream()
                .filter(x -> x.getCategoryId() == category.getCategoryId())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category does not exist"));
        oldcategory.setCategoryName(category.getCategoryName());
        return oldcategory;
    }
}
