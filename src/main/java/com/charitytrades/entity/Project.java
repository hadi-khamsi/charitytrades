package com.charitytrades.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minimumAmount;

    @Column(length = 2000)
    private String description;

    private String imageUrl;

    @Column(name = "global_giving_project_id")
    private Long globalGivingProjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_id", nullable = false)
    private Charity charity;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Project() {}

    public Project(String name, BigDecimal minimumAmount, Charity charity) {
        this.name = name;
        this.minimumAmount = minimumAmount;
        this.charity = charity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getMinimumAmount() { return minimumAmount; }
    public void setMinimumAmount(BigDecimal minimumAmount) { this.minimumAmount = minimumAmount; }

    public Charity getCharity() { return charity; }
    public void setCharity(Charity charity) { this.charity = charity; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getGlobalGivingProjectId() { return globalGivingProjectId; }
    public void setGlobalGivingProjectId(Long globalGivingProjectId) { this.globalGivingProjectId = globalGivingProjectId; }
}
