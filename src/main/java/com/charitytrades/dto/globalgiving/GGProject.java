package com.charitytrades.dto.globalgiving;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GGProject {

    private Long id;
    private String title;
    private String summary;
    private String contactName;
    private String contactAddress;
    private String contactCity;
    private String contactCountry;
    private String contactUrl;
    private BigDecimal goal;
    private BigDecimal funding;
    private BigDecimal remaining;
    private Integer numberOfDonations;
    private String status;
    private String themeName;
    private String imageLink;
    private String projectLink;
    private GGOrganization organization;

    public GGProject() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactAddress() { return contactAddress; }
    public void setContactAddress(String contactAddress) { this.contactAddress = contactAddress; }
    public String getContactCity() { return contactCity; }
    public void setContactCity(String contactCity) { this.contactCity = contactCity; }
    public String getContactCountry() { return contactCountry; }
    public void setContactCountry(String contactCountry) { this.contactCountry = contactCountry; }
    public String getContactUrl() { return contactUrl; }
    public void setContactUrl(String contactUrl) { this.contactUrl = contactUrl; }
    public BigDecimal getGoal() { return goal; }
    public void setGoal(BigDecimal goal) { this.goal = goal; }
    public BigDecimal getFunding() { return funding; }
    public void setFunding(BigDecimal funding) { this.funding = funding; }
    public BigDecimal getRemaining() { return remaining; }
    public void setRemaining(BigDecimal remaining) { this.remaining = remaining; }
    public Integer getNumberOfDonations() { return numberOfDonations; }
    public void setNumberOfDonations(Integer numberOfDonations) { this.numberOfDonations = numberOfDonations; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getThemeName() { return themeName; }
    public void setThemeName(String themeName) { this.themeName = themeName; }
    public String getImageLink() { return imageLink; }
    public void setImageLink(String imageLink) { this.imageLink = imageLink; }
    public String getProjectLink() { return projectLink; }
    public void setProjectLink(String projectLink) { this.projectLink = projectLink; }
    public GGOrganization getOrganization() { return organization; }
    public void setOrganization(GGOrganization organization) { this.organization = organization; }
}
