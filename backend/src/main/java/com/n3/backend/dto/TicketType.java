package com.n3.backend.dto;

import java.sql.Time;
import java.sql.Timestamp;

public class TicketType {
    private String name;
    private double price;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public TicketType(String name, double price, Timestamp createdAt, Timestamp updatedAt) {
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
