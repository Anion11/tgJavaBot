package org.example.entity;

public class AppUser {
    private Long id;
    private Long telegramUserId;
    private String firstName;
    private String lastName;
    private String username;
    private Subscribe subscribe;

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

    public void setSubscribe(Subscribe sub) {
        subscribe = sub;
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

    public Subscribe getSubscribe() {
        return subscribe;
    }

    @Override
    public String toString() {
        return "@" + username +
                " { id=" + id +
                ", telegramUserId=" + telegramUserId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", subscribe=" + subscribe +
                " }";
    }
}