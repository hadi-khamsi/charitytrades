package com.charitytrades.service;

import com.charitytrades.entity.*;
import com.charitytrades.repository.CentralBookRepository;
import com.charitytrades.repository.OrderRepository;
import com.charitytrades.repository.ProjectRepository;
import com.charitytrades.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ExchangeService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final OrderRepository orderRepository;
    private final CentralBookRepository centralBookRepository;

    public ExchangeService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            OrderRepository orderRepository,
            CentralBookRepository centralBookRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.orderRepository = orderRepository;
        this.centralBookRepository = centralBookRepository;
    }

    @Transactional
    public Order placeDonation(Long userId, Long projectId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (amount.compareTo(project.getMinimumAmount()) < 0) {
            Order rejectedOrder = new Order(user, project, amount);
            rejectedOrder.setStatus(OrderStatus.REJECTED);
            return orderRepository.save(rejectedOrder);
        }

        Order order = new Order(user, project, amount);
        order.setStatus(OrderStatus.CONFIRMED);
        order = orderRepository.save(order);

        addOrderToCentralBook(order, project);

        return order;
    }

    private void addOrderToCentralBook(Order order, Project project) {
        CentralBook centralBook = centralBookRepository.findByProjectId(project.getId())
                .orElseGet(() -> {
                    CentralBook newBook = new CentralBook(project);
                    return centralBookRepository.save(newBook);
                });

        centralBook.addOrder(order);
        centralBookRepository.save(centralBook);
    }

    public CentralBook getCentralBookForProject(Long projectId) {
        return centralBookRepository.findByProjectId(projectId).orElse(null);
    }
}
