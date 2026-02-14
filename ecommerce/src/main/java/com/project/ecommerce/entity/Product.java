package com.project.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private int stock;

    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems = new ArrayList<>();
}
