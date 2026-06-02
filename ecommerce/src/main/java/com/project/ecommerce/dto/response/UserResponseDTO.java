package com.project.ecommerce.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String contact;
    private LocalDateTime createdAt;
}
