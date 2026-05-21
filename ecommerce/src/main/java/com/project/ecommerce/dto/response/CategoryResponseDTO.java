package com.project.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CategoryResponseDTO {

    private int id;
    private String name;
    private String description;
}
