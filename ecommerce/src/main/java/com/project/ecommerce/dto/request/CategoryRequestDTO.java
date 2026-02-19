package com.project.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDTO {

    @NotBlank(message = "Category name is Required")
    private String name;

    @NotBlank(message = "Description should be of minimum 50 characters")
    @Min(50)
    private String description;
}
