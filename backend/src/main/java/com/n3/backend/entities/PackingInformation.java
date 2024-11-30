package com.n3.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PackingInformation {
    @Id
    private int id;
    private String name;
    private int totalSlot = 100;
    private int totalSlotAvailable = 100;
    private int maxSlotBooked = 10;
    private int totalSlotBooked = 10;
    private int totalSlotBookedAvailable = 10;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSlot() {
        return totalSlot;
    }

    public void setTotalSlot(int totalSlot) {
        this.totalSlot = totalSlot;
    }

    public int getTotalSlotAvailable() {
        return totalSlotAvailable;
    }

    public void setTotalSlotAvailable(int totalSlotAvailable) {
        this.totalSlotAvailable = totalSlotAvailable;
    }

    public int getMaxSlotBooked() {
        return maxSlotBooked;
    }

    public void setMaxSlotBooked(int maxSlotBooked) {
        this.maxSlotBooked = maxSlotBooked;
    }

    public int getTotalSlotBooked() {
        return totalSlotBooked;
    }

    public void setTotalSlotBooked(int totalSlotBooked) {
        this.totalSlotBooked = totalSlotBooked;
    }

    public int getTotalSlotBookedAvailable() {
        return totalSlotBookedAvailable;
    }

    public void setTotalSlotBookedAvailable(int totalSlotBookedAvailable) {
        this.totalSlotBookedAvailable = totalSlotBookedAvailable;
    }
}
