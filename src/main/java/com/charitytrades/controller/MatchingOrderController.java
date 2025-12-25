package com.charitytrades.controller;

import com.charitytrades.dto.CreateMatchingOrderRequest;
import com.charitytrades.dto.MatchingOrderDTO;
import com.charitytrades.entity.MatchingOrder;
import com.charitytrades.repository.MatchingOrderRepository;
import com.charitytrades.service.ExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matching-orders")
public class MatchingOrderController {

    private final ExchangeService exchangeService;
    private final MatchingOrderRepository matchingOrderRepository;

    public MatchingOrderController(ExchangeService exchangeService, MatchingOrderRepository matchingOrderRepository) {
        this.exchangeService = exchangeService;
        this.matchingOrderRepository = matchingOrderRepository;
    }

    @PostMapping
    public ResponseEntity<?> createMatchingPledge(@RequestBody CreateMatchingOrderRequest request) {
        try {
            MatchingOrder order = exchangeService.createMatchingPledge(
                    request.getCorporateUserId(),
                    request.getProjectId(),
                    request.getMatchRatio(),
                    request.getMaxAmount()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(MatchingOrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<MatchingOrderDTO> getAllMatchingOrders() {
        return matchingOrderRepository.findAll().stream()
                .map(MatchingOrderDTO::fromEntity)
                .toList();
    }

    @GetMapping("/project/{projectId}")
    public List<MatchingOrderDTO> getMatchingOrdersForProject(@PathVariable Long projectId) {
        return matchingOrderRepository.findAvailableMatchersForProject(projectId).stream()
                .map(MatchingOrderDTO::fromEntity)
                .toList();
    }

    @GetMapping("/corporate/{userId}")
    public List<MatchingOrderDTO> getMatchingOrdersByCorporate(@PathVariable Long userId) {
        return matchingOrderRepository.findByCorporateUserId(userId).stream()
                .map(MatchingOrderDTO::fromEntity)
                .toList();
    }
}
