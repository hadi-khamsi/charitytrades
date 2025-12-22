package com.charitytrades.dto;

import com.charitytrades.entity.User;
import com.charitytrades.entity.Order;
import com.charitytrades.entity.OrderStatus;
import java.math.BigDecimal;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private BigDecimal totalDonated;
    private int donationCount;

    public UserDTO() {}

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.email = user.getEmail();

        // Calculate total from confirmed orders
        dto.totalDonated = user.getOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED)
                .map(Order::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.donationCount = (int) user.getOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED)
                .count();

        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getTotalDonated() { return totalDonated; }
    public void setTotalDonated(BigDecimal totalDonated) { this.totalDonated = totalDonated; }
    public int getDonationCount() { return donationCount; }
    public void setDonationCount(int donationCount) { this.donationCount = donationCount; }
}
