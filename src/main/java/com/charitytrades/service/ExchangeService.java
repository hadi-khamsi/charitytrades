package com.charitytrades.service;

import com.charitytrades.entity.*;
import com.charitytrades.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ExchangeService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final OrderRepository orderRepository;
    private final CentralBookRepository centralBookRepository;
    private final MatchingOrderRepository matchingOrderRepository;
    private final OrderRouter orderRouter;

    public ExchangeService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            OrderRepository orderRepository,
            CentralBookRepository centralBookRepository,
            MatchingOrderRepository matchingOrderRepository,
            OrderRouter orderRouter) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.orderRepository = orderRepository;
        this.centralBookRepository = centralBookRepository;
        this.matchingOrderRepository = matchingOrderRepository;
        this.orderRouter = orderRouter;
    }

    @Transactional
    public Order placeDonation(Long userId, Long projectId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        Order order = new Order(user, project, amount);
        order.setStatus(OrderStatus.NEW);
        order = orderRepository.save(order);

        order = validateOrder(order, project);
        if (order.getStatus() == OrderStatus.REJECTED) {
            return order;
        }

        order = routeOrder(order);
        order = executeOrder(order);

        return order;
    }

    private Order validateOrder(Order order, Project project) {
        if (order.getAmount().compareTo(project.getMinimumAmount()) < 0) {
            order.setStatus(OrderStatus.REJECTED);
            return orderRepository.save(order);
        }

        order.setStatus(OrderStatus.VALIDATED);
        return orderRepository.save(order);
    }

    private Order routeOrder(Order order) {
        RouteType route = orderRouter.determineRoute(order);
        order.setRouteType(route);
        order.setStatus(OrderStatus.ROUTED);
        return orderRepository.save(order);
    }

    private Order executeOrder(Order order) {
        if (order.getRouteType() == RouteType.CLOB) {
            return executeViaCLOB(order);
        } else {
            return executeDirect(order);
        }
    }

    private Order executeViaCLOB(Order order) {
        CentralBook clob = getOrCreateCentralBook(order.getProject());
        clob.addBid(order);

        MatchingOrder matcher = orderRouter.findBestMatcher(order.getProject().getId());
        if (matcher != null && matcher.isAvailable()) {
            BigDecimal matchAmount = matcher.calculateMatchAmount(order.getAmount());
            matcher.deductFromRemaining(matchAmount);
            matchingOrderRepository.save(matcher);

            order.setMatchedWith(matcher);
            order.setMatchedAmount(matchAmount);
            order.setTotalImpact(order.getAmount().add(matchAmount));
            order.setStatus(OrderStatus.MATCHED);
        } else {
            order.setTotalImpact(order.getAmount());
            order.setStatus(OrderStatus.EXECUTED);
        }

        order.setDonationLink(generateDonationLink(order));
        centralBookRepository.save(clob);
        return orderRepository.save(order);
    }

    private Order executeDirect(Order order) {
        order.setTotalImpact(order.getAmount());
        order.setStatus(OrderStatus.EXECUTED);
        order.setDonationLink(generateDonationLink(order));

        CentralBook clob = getOrCreateCentralBook(order.getProject());
        clob.addBid(order);
        centralBookRepository.save(clob);

        return orderRepository.save(order);
    }

    private String generateDonationLink(Order order) {
        Long ggProjectId = order.getProject().getGlobalGivingProjectId();
        if (ggProjectId != null) {
            return "https://www.globalgiving.org/donate/" + ggProjectId;
        }
        return "https://www.globalgiving.org/search/";
    }

    @Transactional
    public Order settleOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.MATCHED && order.getStatus() != OrderStatus.EXECUTED) {
            throw new RuntimeException("Order cannot be settled in current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CLEARING);
        order = orderRepository.save(order);

        order.setStatus(OrderStatus.SETTLED);
        order.setSettledAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public MatchingOrder createMatchingPledge(Long corporateUserId, Long projectId, BigDecimal matchRatio, BigDecimal maxAmount) {
        User user = userRepository.findById(corporateUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + corporateUserId));

        if (!user.isCorporate()) {
            throw new RuntimeException("Only corporate accounts can create matching pledges");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        MatchingOrder matchingOrder = new MatchingOrder(user, project, matchRatio, maxAmount);
        matchingOrder = matchingOrderRepository.save(matchingOrder);

        CentralBook clob = getOrCreateCentralBook(project);
        clob.addAsk(matchingOrder);
        centralBookRepository.save(clob);

        return matchingOrder;
    }

    private CentralBook getOrCreateCentralBook(Project project) {
        return centralBookRepository.findByProjectId(project.getId())
                .orElseGet(() -> {
                    CentralBook newBook = new CentralBook(project);
                    return centralBookRepository.save(newBook);
                });
    }
}
