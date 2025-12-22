package com.charitytrades.controller;

import com.charitytrades.dto.OrderDTO;
import com.charitytrades.dto.PlaceOrderRequest;
import com.charitytrades.entity.Order;
import com.charitytrades.service.ExchangeService;
import com.charitytrades.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ExchangeService exchangeService;
    private final OrderService orderService;

    public OrderController(ExchangeService exchangeService, OrderService orderService) {
        this.exchangeService = exchangeService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> placeBuyOrder(@RequestBody PlaceOrderRequest request) {
        try {
            Order order = exchangeService.placeBuyOrder(
                    request.getUserId(),
                    request.getProjectId(),
                    request.getAmount()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> ResponseEntity.ok(OrderDTO.fromEntity(order)))
                .orElse(ResponseEntity.notFound().build());
    }
}
