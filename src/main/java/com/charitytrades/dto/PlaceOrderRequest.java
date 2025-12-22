package com.charitytrades.dto;

import java.math.BigDecimal;

public class PlaceOrderRequest {
    private Long userId;
    private Long projectId;
    private BigDecimal amount;

    public PlaceOrderRequest() {}

    public PlaceOrderRequest(Long userId, Long projectId, BigDecimal amount) {
        this.userId = userId;
        this.projectId = projectId;
        this.amount = amount;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
