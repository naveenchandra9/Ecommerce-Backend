package com.project.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OrderItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product products;

}
