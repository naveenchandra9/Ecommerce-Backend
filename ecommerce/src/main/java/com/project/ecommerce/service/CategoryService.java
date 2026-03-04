package com.project.ecommerce.service;

import com.project.ecommerce.dto.request.CategoryRequestDTO;
import com.project.ecommerce.dto.response.CategoryResponseDTO;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepo categoryRepo, ModelMapper modelMapper){
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO){
        logger.info("Creating new category with name {}", categoryRequestDTO.getName());

        // Check category already exists
        if(categoryRepo.existsByName(categoryRequestDTO.getName())){
            throw new RuntimeException("Category already exists");
        }

        Category category = Category.builder()
                .name(categoryRequestDTO.getName())
                .description(categoryRequestDTO.getDescription())
                .build();

        Category savedCategory = categoryRepo.save(category);

        logger.info("Category {} created successfully", categoryRequestDTO.getName());

        return convertToDTOResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories(){
        List<Category> categories = categoryRepo.findAll();
        return categories.stream()
                .map(this::convertToDTOResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(int id){
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return convertToDTOResponse(category);
    }

    public CategoryResponseDTO convertToDTOResponse(Category category){
        CategoryResponseDTO dto = modelMapper.map(category, CategoryResponseDTO.class);
        return dto;
    }
}
