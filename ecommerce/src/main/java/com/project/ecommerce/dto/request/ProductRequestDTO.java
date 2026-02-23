package com.project.ecommerce.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "Product Name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    @Min(10)
    private String description;

    @NotBlank(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank
    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    @NotBlank
    private int categoryId;
}
