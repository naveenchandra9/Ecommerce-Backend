package com.project.ecommerce.dto.response;

import com.project.ecommerce.entity.Order;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private int id;
    private int userId;
    private String userName;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal orderAmount;
    private List<OrderItemsResponseDTO> orderItems;


    public static class OrderItemsResponseDTO {

        private int productId;
        private BigDecimal price;
        private int quantity;
        private String productName;
    }

}
