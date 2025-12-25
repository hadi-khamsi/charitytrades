package com.charitytrades.service;

import com.charitytrades.entity.Order;
import com.charitytrades.entity.RouteType;
import com.charitytrades.entity.MatchingOrder;
import com.charitytrades.repository.MatchingOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderRouter {

    private final MatchingOrderRepository matchingOrderRepository;

    public OrderRouter(MatchingOrderRepository matchingOrderRepository) {
        this.matchingOrderRepository = matchingOrderRepository;
    }

    public RouteType determineRoute(Order order) {
        List<MatchingOrder> availableMatchers = matchingOrderRepository
                .findAvailableMatchersForProject(order.getProject().getId());

        if (availableMatchers.isEmpty()) {
            return RouteType.DIRECT;
        }

        return RouteType.CLOB;
    }

    public MatchingOrder findBestMatcher(Long projectId) {
        List<MatchingOrder> matchers = matchingOrderRepository
                .findAvailableMatchersForProject(projectId);

        if (matchers.isEmpty()) {
            return null;
        }

        return matchers.stream()
                .max((a, b) -> a.getMatchRatio().compareTo(b.getMatchRatio()))
                .orElse(matchers.get(0));
    }
}
