package com.project.ecommerce.controller;

import com.project.ecommerce.dto.request.OrderRequestDTO;
import com.project.ecommerce.dto.response.OrderResponseDTO;
import com.project.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    /**
     * POST /api/orders - Create new order
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){
        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    /**
     * GET /api/orders/{id} - Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id){
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    /**
     * GET /api/orders/user/{userId} - Get all orders for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(@PathVariable Long userId){
        List<OrderResponseDTO> orderResponseDTOS = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orderResponseDTOS);
    }

    /**
     * PUT /api/orders/{id}/cancel - Cancel order
     */
    @GetMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id){
        OrderResponseDTO orderResponseDTO = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
