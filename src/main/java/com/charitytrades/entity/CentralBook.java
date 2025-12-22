package com.charitytrades.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "central_books")
public class CentralBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "central_book_orders",
        joinColumns = @JoinColumn(name = "central_book_id"),
        inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders = new ArrayList<>();

    public CentralBook() {}

    public CentralBook(Project project) {
        this.project = project;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}
