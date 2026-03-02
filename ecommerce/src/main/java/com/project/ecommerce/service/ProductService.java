package com.project.ecommerce.service;

import com.project.ecommerce.dto.request.ProductRequestDTO;
import com.project.ecommerce.dto.response.ProductResponseDTO;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.exception.ProductNotFoundException;
import com.project.ecommerce.repository.CategoryRepo;
import com.project.ecommerce.repository.ProductRepo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    // Constructor Injection (Recommended)
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    // Field Injection (for demonstration)
    @Autowired
    private CategoryService categoryService;

    /**
     * Constructor Injection - Best practice for DI
     */
    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo, ModelMapper modelMapper){
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * Bean Lifecycle: @PostConstruct
     */
    @PostConstruct
    public void init(){
        logger.info("ProductService initialized");
    }

    /**
     * Bean Lifecycle: @PreDestroy
     */
    @PreDestroy
    public void cleanup(){
        logger.info("ProductService cleanup");
    }

    /**
     * Create new product
     */
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO){
        // Validate category exist
        Category category = categoryRepo.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create product entity
        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .stock(productRequestDTO.getStock())
                .category(category)
                .active(true)
                .build();

        // Save to database
        Product savedProduct = productRepo.save(product);

        return convertToResponseDTO(savedProduct);
    }

    /**
     * Get product by ID
     */
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id){
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: "+id));

        return convertToResponseDTO(product);
    }

    /**
     * Get all active products with pagination
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable){
        Page<Product> products = productRepo.findByActiveTrue(pageable);
        return products.map(this::convertToResponseDTO);
    }

    /**
     * Update product
     */
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO){
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: "+id +" not found"));
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setStock(productRequestDTO.getStock());

        if(product.getCategory().getId() != productRequestDTO.getCategoryId()){
            Category category = categoryRepo.findById(productRequestDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepo.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    /**
     * Soft delete product (mark as inactive)
     */
    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: "+id +" not found"));

        product.setActive(false);
        productRepo.save(product);
    }

    /**
     * Search products by category
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory(int categoryId, Pageable pageable){
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Page<Product> products = productRepo.findByCategoryIdAndActiveTrue(categoryId, pageable);
        return products.map(this::convertToResponseDTO);
    }

    /**
     * Search products by price range
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable){
        Page<Product> products = productRepo.findByPriceRange(minPrice, maxPrice, pageable);
        return products.map(this::convertToResponseDTO);
    }

    public Page<ProductResponseDTO> getProductsByCategoryAndPriceRange(int categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable){
        Page<Product> products = productRepo.findByCategoryIdAndPriceRange(categoryId, minPrice, maxPrice, pageable);
        return products.map(this::convertToResponseDTO);
    }

    /**
     * Convert Product entity to ProductResponseDTO
     */
    public ProductResponseDTO convertToResponseDTO(Product product){
        ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }



}
