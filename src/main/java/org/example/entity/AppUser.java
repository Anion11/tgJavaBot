package org.example.entity;

public class AppUser {
    private Long id;

    private Long telegramUserId;

    private String firstName;

    private String lastName;

    private String username;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getActive() {
        return isActive;
    }
}