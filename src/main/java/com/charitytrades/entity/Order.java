package com.charitytrades.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    @Enumerated(EnumType.STRING)
    private RouteType routeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_with_id")
    private MatchingOrder matchedWith;

    @Column(precision = 19, scale = 2)
    private BigDecimal matchedAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalImpact;

    private String donationLink;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime settledAt;

    private String statusMessage;

    public Order() {}

    public Order(User user, Project project, BigDecimal amount) {
        this.user = user;
        this.project = project;
        this.amount = amount;
        this.status = OrderStatus.NEW;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public RouteType getRouteType() { return routeType; }
    public void setRouteType(RouteType routeType) { this.routeType = routeType; }

    public MatchingOrder getMatchedWith() { return matchedWith; }
    public void setMatchedWith(MatchingOrder matchedWith) { this.matchedWith = matchedWith; }

    public BigDecimal getMatchedAmount() { return matchedAmount; }
    public void setMatchedAmount(BigDecimal matchedAmount) { this.matchedAmount = matchedAmount; }

    public BigDecimal getTotalImpact() { return totalImpact; }
    public void setTotalImpact(BigDecimal totalImpact) { this.totalImpact = totalImpact; }

    public String getDonationLink() { return donationLink; }
    public void setDonationLink(String donationLink) { this.donationLink = donationLink; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
}
