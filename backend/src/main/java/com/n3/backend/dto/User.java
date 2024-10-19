package com.n3.backend.dto;

import com.n3.backend.entities.UserEntity;

public class User {
    private String fullname;
    private String email;
    private String address;
    private String phoneNumber;
    private String dateOfBirth;
    private double balance;
    private String position;
    private String createdAt;
    private String updatedAt;

    public User(UserEntity user){
        setAddress(user.getAddress());
        setBalance(user.getBalance());
        setEmail(user.getEmail());
        setFullname(user.getFullname());
        setPosition(user.getPosition());
        setCreatedAt(user.getCreatedAt().toLocalDate().toString());
        setPhoneNumber(user.getPhoneNumber());
        setDateOfBirth(user.getDateOfBirth().toLocalDate().toString());
    }
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
