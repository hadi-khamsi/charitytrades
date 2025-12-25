package com.charitytrades.controller;

import com.charitytrades.dto.OrderDTO;
import com.charitytrades.dto.UserDTO;
import com.charitytrades.service.OrderService;
import com.charitytrades.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserDTO.fromEntity(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long id) {
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<OrderDTO> orders = orderService.findByUserId(id).stream()
                .map(OrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(orders);
    }
}
