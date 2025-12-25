package com.charitytrades.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "matching_orders")
public class MatchingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporate_user_id", nullable = false)
    private User corporateUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal matchRatio;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal maxAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal remainingAmount;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt;

    public MatchingOrder() {}

    public MatchingOrder(User corporateUser, Project project, BigDecimal matchRatio, BigDecimal maxAmount) {
        this.corporateUser = corporateUser;
        this.project = project;
        this.matchRatio = matchRatio;
        this.maxAmount = maxAmount;
        this.remainingAmount = maxAmount;
        this.createdAt = LocalDateTime.now();
    }

    public BigDecimal calculateMatchAmount(BigDecimal donationAmount) {
        BigDecimal potentialMatch = donationAmount.multiply(matchRatio);
        return potentialMatch.min(remainingAmount);
    }

    public void deductFromRemaining(BigDecimal amount) {
        this.remainingAmount = this.remainingAmount.subtract(amount);
        if (this.remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            this.active = false;
        }
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isAvailable() {
        return active && !isExpired() && remainingAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCorporateUser() { return corporateUser; }
    public void setCorporateUser(User corporateUser) { this.corporateUser = corporateUser; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public BigDecimal getMatchRatio() { return matchRatio; }
    public void setMatchRatio(BigDecimal matchRatio) { this.matchRatio = matchRatio; }

    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }

    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
