package com.n3.backend.dto.User;

import com.n3.backend.utils.DatetimeConvert;
import com.n3.backend.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String fullname;
    private String email;
    private String address;
    private String phoneNumber;
    private String dateOfBirth;
    private double balance;
    private String position;
    private String createdAt;
    private String updatedAt;
    private boolean isAdmin;

    public User(UserEntity user){
        setId(user.getId());
        setAddress(user.getAddress());
//        setBalance(user.getBalance());
        setEmail(user.getEmail());
        setFullname(user.getFullname());
        setPosition(user.getPosition());
        setAdmin(user.isAdmin());
        setUpdatedAt(DatetimeConvert.timastampToString(user.getUpdatedAt()));
        setCreatedAt(DatetimeConvert.timastampToString(user.getCreatedAt()));
        setPhoneNumber(user.getPhoneNumber());
        setDateOfBirth(user.getDateOfBirth() != null ? DatetimeConvert.dateToString(user.getDateOfBirth()) : null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<User> getList(List<UserEntity> list){
        List<User> result = new ArrayList<>();
        for (UserEntity user : list){
            result.add(new User(user));
        }
        return result;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
