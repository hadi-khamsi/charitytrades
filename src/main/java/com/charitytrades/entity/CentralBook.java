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
        name = "clob_bids",
        joinColumns = @JoinColumn(name = "central_book_id"),
        inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> bids = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "clob_asks",
        joinColumns = @JoinColumn(name = "central_book_id"),
        inverseJoinColumns = @JoinColumn(name = "matching_order_id")
    )
    private List<MatchingOrder> asks = new ArrayList<>();

    public CentralBook() {}

    public CentralBook(Project project) {
        this.project = project;
    }

    public void addBid(Order order) {
        this.bids.add(order);
    }

    public void addAsk(MatchingOrder matchingOrder) {
        this.asks.add(matchingOrder);
    }

    public void removeBid(Order order) {
        this.bids.remove(order);
    }

    public List<MatchingOrder> getAvailableAsks() {
        return asks.stream()
                .filter(MatchingOrder::isAvailable)
                .toList();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<Order> getBids() { return bids; }
    public void setBids(List<Order> bids) { this.bids = bids; }

    public List<MatchingOrder> getAsks() { return asks; }
    public void setAsks(List<MatchingOrder> asks) { this.asks = asks; }
}
