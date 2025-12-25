package com.charitytrades.dto;

import com.charitytrades.entity.MatchingOrder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MatchingOrderDTO {
    private Long id;
    private Long corporateUserId;
    private String corporateUserName;
    private Long projectId;
    private String projectName;
    private BigDecimal matchRatio;
    private BigDecimal maxAmount;
    private BigDecimal remainingAmount;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public MatchingOrderDTO() {}

    public static MatchingOrderDTO fromEntity(MatchingOrder entity) {
        MatchingOrderDTO dto = new MatchingOrderDTO();
        dto.id = entity.getId();
        dto.corporateUserId = entity.getCorporateUser().getId();
        dto.corporateUserName = entity.getCorporateUser().getUsername();
        dto.projectId = entity.getProject().getId();
        dto.projectName = entity.getProject().getName();
        dto.matchRatio = entity.getMatchRatio();
        dto.maxAmount = entity.getMaxAmount();
        dto.remainingAmount = entity.getRemainingAmount();
        dto.active = entity.isActive();
        dto.createdAt = entity.getCreatedAt();
        dto.expiresAt = entity.getExpiresAt();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCorporateUserId() { return corporateUserId; }
    public void setCorporateUserId(Long corporateUserId) { this.corporateUserId = corporateUserId; }
    public String getCorporateUserName() { return corporateUserName; }
    public void setCorporateUserName(String corporateUserName) { this.corporateUserName = corporateUserName; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
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
