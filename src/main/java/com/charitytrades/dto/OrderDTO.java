package com.charitytrades.dto;

import com.charitytrades.entity.Order;
import com.charitytrades.entity.OrderStatus;
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
    private LocalDateTime createdAt;

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
        dto.createdAt = order.getCreatedAt();
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
