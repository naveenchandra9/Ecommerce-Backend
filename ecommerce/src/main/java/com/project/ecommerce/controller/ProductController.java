package com.project.ecommerce.controller;

import com.project.ecommerce.dto.request.ProductRequestDTO;
import com.project.ecommerce.dto.response.ProductResponseDTO;
import com.project.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    /**
     * POST /api/products - Create new product
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO responseDTO = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * GET /api/products/{id} - Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable  int id){
        ProductResponseDTO responseDTO = productService.getProductById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * GET /api/products - Get all products (paginated)
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(Pageable pageable){
        Page<ProductResponseDTO> responseDTOS = productService.getAllProducts(pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    /**
     * PUT /api/products/{id} - Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO responseDTO = productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * DELETE /api/products/{id} - Soft delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(int id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/products/category/{categoryId} - Get products by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(@PathVariable int categoryId, Pageable pageable){
        Page<ProductResponseDTO> productResponseDTOS = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(productResponseDTOS);
    }

    /**
     * GET /api/products/search - Search products by price range
     * Example: /api/products/search?minPrice=10&maxPrice=100
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice, Pageable pageable){
        Page<ProductResponseDTO> productResponseDTOS;

        if(categoryId!=null && minPrice!=null && maxPrice!=null){
            productResponseDTOS = productService.getProductsByCategoryAndPriceRange(categoryId, minPrice, maxPrice, pageable);
        }
        else if(minPrice!=null && maxPrice!=null){
            productResponseDTOS = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        }
        else if(categoryId!=null){
            productResponseDTOS = productService.getProductsByCategory(categoryId, pageable);
        }
        else{
            productResponseDTOS = productService.getAllProducts(pageable);
        }

        return ResponseEntity.ok(productResponseDTOS);
    }
}
