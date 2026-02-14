package com.project.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(precision = 10, scale = 2)
    private BigDecimal total_amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @DateTimeFormat
    @Column
    private Date order_date;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems = new ArrayList<>();

    public enum OrderStatus{
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

}
