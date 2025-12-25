package com.charitytrades.dto;

import com.charitytrades.entity.Order;
import com.charitytrades.entity.OrderStatus;
import com.charitytrades.entity.RouteType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long projectId;
    private String projectName;
    private BigDecimal amount;
    private OrderStatus status;
    private RouteType routeType;
    private BigDecimal matchedAmount;
    private BigDecimal totalImpact;
    private String matchedWithCorporate;
    private String donationLink;
    private LocalDateTime createdAt;
    private LocalDateTime settledAt;
    private String statusMessage;

    public OrderDTO() {}

    public static OrderDTO fromEntity(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.id = order.getId();
        dto.userId = order.getUser().getId();
        dto.userName = order.getUser().getName();
        dto.projectId = order.getProject().getId();
        dto.projectName = order.getProject().getName();
        dto.amount = order.getAmount();
        dto.status = order.getStatus();
        dto.routeType = order.getRouteType();
        dto.matchedAmount = order.getMatchedAmount();
        dto.totalImpact = order.getTotalImpact();
        dto.donationLink = order.getDonationLink();
        dto.createdAt = order.getCreatedAt();
        dto.settledAt = order.getSettledAt();
        dto.statusMessage = order.getStatusMessage();
        if (order.getMatchedWith() != null) {
            dto.matchedWithCorporate = order.getMatchedWith().getCorporateUser().getUsername();
        }
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public RouteType getRouteType() { return routeType; }
    public void setRouteType(RouteType routeType) { this.routeType = routeType; }
    public BigDecimal getMatchedAmount() { return matchedAmount; }
    public void setMatchedAmount(BigDecimal matchedAmount) { this.matchedAmount = matchedAmount; }
    public BigDecimal getTotalImpact() { return totalImpact; }
    public void setTotalImpact(BigDecimal totalImpact) { this.totalImpact = totalImpact; }
    public String getMatchedWithCorporate() { return matchedWithCorporate; }
    public void setMatchedWithCorporate(String matchedWithCorporate) { this.matchedWithCorporate = matchedWithCorporate; }
    public String getDonationLink() { return donationLink; }
    public void setDonationLink(String donationLink) { this.donationLink = donationLink; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }
    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
}
