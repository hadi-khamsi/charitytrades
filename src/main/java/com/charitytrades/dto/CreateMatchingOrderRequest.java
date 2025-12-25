package com.charitytrades.dto;

import java.math.BigDecimal;

public class CreateMatchingOrderRequest {
    private Long corporateUserId;
    private Long projectId;
    private BigDecimal matchRatio;
    private BigDecimal maxAmount;

    public CreateMatchingOrderRequest() {}

    public Long getCorporateUserId() { return corporateUserId; }
    public void setCorporateUserId(Long corporateUserId) { this.corporateUserId = corporateUserId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public BigDecimal getMatchRatio() { return matchRatio; }
    public void setMatchRatio(BigDecimal matchRatio) { this.matchRatio = matchRatio; }
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
}
