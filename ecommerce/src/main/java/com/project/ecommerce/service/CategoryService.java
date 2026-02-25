package com.project.ecommerce.service;

import com.project.ecommerce.dto.request.CategoryRequestDTO;
import com.project.ecommerce.dto.response.CategoryResponseDTO;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
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

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO){
        // Check category already exists
        if(categoryRepo.existsByName(categoryRequestDTO.getName())){
            throw new RuntimeException("Category already exists");
        }

        Category category = Category.builder()
                .name(categoryRequestDTO.getName())
                .description(categoryRequestDTO.getDescription())
                .build();

        Category savedCategory = categoryRepo.save(category);

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
