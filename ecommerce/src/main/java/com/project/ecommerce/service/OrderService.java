package com.project.ecommerce.service;

import com.project.ecommerce.dto.request.OrderRequestDTO;
import com.project.ecommerce.dto.response.OrderResponseDTO;
import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.OrderItems;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.exception.InsufficientStockException;
import com.project.ecommerce.exception.OrderNotFoundException;
import com.project.ecommerce.exception.ProductNotFoundException;
import com.project.ecommerce.repository.OrderRepo;
import com.project.ecommerce.repository.ProductRepo;
import com.project.ecommerce.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepo orderRepo, ProductRepo productRepo, UserRepo userRepo, ModelMapper modelMapper){
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * Create order - Transactional operation
     * Propagation.REQUIRED (default): Use existing transaction or create new
     * Isolation.READ_COMMITTED: Prevent dirty reads
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO){
        logger.info("Creating order for user: {}", orderRequestDTO.getUserId());

        User user = userRepo.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(user)
                .order_date(LocalDateTime.now())
                .total_amount(totalAmount)
                .status(Order.OrderStatus.PENDING)
                .build();

        for(OrderRequestDTO.OrderItemDTO itemDTO : orderRequestDTO.getOrderItems()){
            Product product = productRepo.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Prodduct with id :" +itemDTO.getProductId() +"not found."));

            // Check if sufficient stock available
            if(itemDTO.getQuantity() > product.getStock()){
                throw new InsufficientStockException("Insufficient stock for Product "+product.getName());
            }

            // Deduct stock
            product.setStock(product.getStock() - itemDTO.getQuantity());
            productRepo.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

            // Create Order item
            OrderItems orderItems = OrderItems.builder()
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .order(order)
                    .price(product.getPrice())
                    .build();

            order.getOrderItems().add(orderItems);
            totalAmount = totalAmount.add(itemTotal);
        }

        // Set total Order amount
        order.setTotal_amount(totalAmount);

        // Update Order status
        order.setStatus(Order.OrderStatus.CONFIRMED);

        Order savedOrder = orderRepo.save(order);

        logger.info("Order created successfully: {}", savedOrder.getId());

        return convertToResponseDTO(savedOrder);
    }

    /**
     * Cancel order - Restore stock (Transactional rollback scenario)
     * Propagation.REQUIRES_NEW: Always create new transaction
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderResponseDTO cancelOrder(int orderId){
        logger.info("Cancelling order for id: {}", orderId);

        // Check if order exists
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: "+orderId +" not found"));

        // Check if order can be cancelled
        if(order.getStatus() == Order.OrderStatus.CANCELLED){
            throw new RuntimeException("Order already in CANCELLED state");
        }

        if(order.getStatus() == Order.OrderStatus.DELIVERED){
            throw new RuntimeException("Delivered Order cannot be cancelled");
        }

        // Restore stock for each item
        for(OrderItems item : order.getOrderItems()){
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepo.save(product);
        }

        // Update Order status
        order.setStatus(Order.OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepo.save(order);

        logger.info("Order cancelled successfully for order id {}", orderId);

        return convertToResponseDTO(cancelledOrder);
    }

    /**
     * Get order by ID
     */
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(int orderId){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: "+orderId +" not found"));

        return convertToResponseDTO(order);
    }

    /**
     * Get all orders for a user
     */
    public List<OrderResponseDTO> getUserOrders(int userId){
        List<Order> orders = orderRepo.findByUserId(userId);
        return orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO convertToResponseDTO(Order order){
        OrderResponseDTO dto = modelMapper.map(order, OrderResponseDTO.class);
        return dto;
    }
}
