package com.charitytrades.dto.globalgiving;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GGOrganization {

    private Long id;
    private String name;
    private String mission;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postal;
    private String country;
    private String url;
    private String logoUrl;
    private Integer totalProjects;
    private Integer activeProjects;

    public GGOrganization() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMission() { return mission; }
    public void setMission(String mission) { this.mission = mission; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPostal() { return postal; }
    public void setPostal(String postal) { this.postal = postal; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public Integer getTotalProjects() { return totalProjects; }
    public void setTotalProjects(Integer totalProjects) { this.totalProjects = totalProjects; }
    public Integer getActiveProjects() { return activeProjects; }
    public void setActiveProjects(Integer activeProjects) { this.activeProjects = activeProjects; }
}
