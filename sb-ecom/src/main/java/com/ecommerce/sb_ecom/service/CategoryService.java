package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

public interface CategoryService
{

    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO);
    CategoryResponse getAllCategories( Integer pageIndex,   Integer pageNumber, String sortBy, String sortOrder);
}
