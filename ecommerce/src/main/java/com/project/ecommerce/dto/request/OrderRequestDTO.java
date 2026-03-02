package com.project.ecommerce.dto.request;

import com.project.ecommerce.entity.OrderItems;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotNull(message = "UserId is required")
    private Long userId;

    private LocalDateTime createdAt;

    @NotEmpty
    List<OrderItemDTO> orderItems;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDTO{

        @NotNull(message = "ProductId is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Minimum 1 quantity must be added")
        private int quantity;
    }
}
