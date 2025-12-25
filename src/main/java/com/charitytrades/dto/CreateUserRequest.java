package com.charitytrades.dto;

import com.charitytrades.entity.AccountType;

public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private AccountType accountType;

    public CreateUserRequest() {}

    public CreateUserRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountType = AccountType.PERSONAL;
    }

    public CreateUserRequest(String username, String email, String password, AccountType accountType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}
