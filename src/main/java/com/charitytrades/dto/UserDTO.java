package com.charitytrades.dto;

import com.charitytrades.entity.User;
import com.charitytrades.entity.Order;
import com.charitytrades.entity.OrderStatus;
import com.charitytrades.entity.AccountType;
import java.math.BigDecimal;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private AccountType accountType;
    private BigDecimal totalDonated;
    private BigDecimal totalImpact;
    private int donationCount;

    public UserDTO() {}

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.accountType = user.getAccountType();

        dto.totalDonated = user.getOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.SETTLED || o.getStatus() == OrderStatus.MATCHED || o.getStatus() == OrderStatus.EXECUTED)
                .map(Order::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.totalImpact = user.getOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.SETTLED || o.getStatus() == OrderStatus.MATCHED || o.getStatus() == OrderStatus.EXECUTED)
                .map(o -> o.getTotalImpact() != null ? o.getTotalImpact() : o.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.donationCount = (int) user.getOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.SETTLED || o.getStatus() == OrderStatus.MATCHED || o.getStatus() == OrderStatus.EXECUTED)
                .count();

        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public BigDecimal getTotalDonated() { return totalDonated; }
    public void setTotalDonated(BigDecimal totalDonated) { this.totalDonated = totalDonated; }
    public BigDecimal getTotalImpact() { return totalImpact; }
    public void setTotalImpact(BigDecimal totalImpact) { this.totalImpact = totalImpact; }
    public int getDonationCount() { return donationCount; }
    public void setDonationCount(int donationCount) { this.donationCount = donationCount; }
}
