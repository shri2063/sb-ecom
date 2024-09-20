package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.service.CategoryService;
import com.ecommerce.sb_ecom.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController
{


    private CategoryService categoryService ;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    //@RequestMapping(value =  "/api/public/categories", method =  RequestMethod.GET)
    @GetMapping("/api/public/categories")
    public ResponseEntity<List<Category>> getAllCategories()
    {
        return  ResponseEntity.ok(this.categoryService.getAllCategories());
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category)
    {
        this.categoryService.createCategory(category);
        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/api/public/categories")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category)
    {
        try {
            Category updatedCategory = categoryService.updateCategory(category);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        }
        catch (ResponseStatusException ex)
        {
            return new ResponseEntity<>(null, ex.getStatusCode());
        }


    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId )
    {

        try
            {
                return new ResponseEntity<String>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
            }
        catch (ResponseStatusException ex)
        {
            System.out.println(ex.getReason());
            return  new ResponseEntity<String>(ex.getReason(), ex.getStatusCode());
        }


    }


}
