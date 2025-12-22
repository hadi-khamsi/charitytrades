package com.charitytrades.dto;

import com.charitytrades.entity.Project;
import java.math.BigDecimal;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal minimumAmount;
    private String charityName;
    private Long charityId;
    private Long globalGivingProjectId;

    public ProjectDTO() {}

    public static ProjectDTO fromEntity(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.id = project.getId();
        dto.name = project.getName();
        dto.description = project.getDescription();
        dto.imageUrl = project.getImageUrl();
        dto.minimumAmount = project.getMinimumAmount();
        dto.charityName = project.getCharity().getName();
        dto.charityId = project.getCharity().getId();
        dto.globalGivingProjectId = project.getGlobalGivingProjectId();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public BigDecimal getMinimumAmount() { return minimumAmount; }
    public void setMinimumAmount(BigDecimal minimumAmount) { this.minimumAmount = minimumAmount; }
    public String getCharityName() { return charityName; }
    public void setCharityName(String charityName) { this.charityName = charityName; }
    public Long getCharityId() { return charityId; }
    public void setCharityId(Long charityId) { this.charityId = charityId; }
    public Long getGlobalGivingProjectId() { return globalGivingProjectId; }
    public void setGlobalGivingProjectId(Long globalGivingProjectId) { this.globalGivingProjectId = globalGivingProjectId; }
}
