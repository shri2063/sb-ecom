package com.ecommerce.sb_ecom.repositories;

import com.ecommerce.sb_ecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
   public Optional<Category> findByCategoryName(String name);
}
