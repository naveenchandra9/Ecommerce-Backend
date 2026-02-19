package com.project.ecommerce.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private int id;
    private String name;
    private String email;
    private Long contact;
    private LocalDateTime createdAt;
}
