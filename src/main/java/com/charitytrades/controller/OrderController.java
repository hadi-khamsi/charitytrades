package com.charitytrades.controller;

import com.charitytrades.dto.OrderDTO;
import com.charitytrades.dto.PlaceOrderRequest;
import com.charitytrades.entity.Order;
import com.charitytrades.service.ExchangeService;
import com.charitytrades.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<?> placeDonationOrder(@RequestBody PlaceOrderRequest request) {
        try {
            Order order = exchangeService.placeDonation(
                    request.getUserId(),
                    request.getProjectId(),
                    request.getAmount()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> ResponseEntity.ok(OrderDTO.fromEntity(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/settle")
    public ResponseEntity<?> settleOrder(@PathVariable Long id) {
        try {
            Order order = exchangeService.settleOrder(id);
            return ResponseEntity.ok(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
