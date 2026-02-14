package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    //find all active products
    Page<Product> findByActiveTrue(Pageable pageable);

    //find products by category
    Page<Product> findByCategoryIdAndActiveTrue(int category_id, Pageable pageable);

    //find products within price range
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.category.id = :categoryId AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByCategoryIdAndPriceRange(@Param("categoryId") int category_id, @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);
}
