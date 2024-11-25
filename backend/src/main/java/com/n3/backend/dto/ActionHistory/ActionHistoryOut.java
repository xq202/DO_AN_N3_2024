package com.n3.backend.dto.ActionHistory;

import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.Ticket.Ticket;

import java.sql.Timestamp;

public class ActionHistoryOut {
    private Car car;
    private String action;
    private String createdAt;
    private double price;
    private Ticket ticket;

    public ActionHistoryOut() {
    }

    public ActionHistoryOut(Car car, String action, String createdAt, double price, Ticket ticket) {
        this.car = car;
        this.action = action;
        this.createdAt = createdAt;
        this.price = price;
        this.ticket = ticket;
    }

    public Car getCar() {
        return car;
    }

    public String getAction() {
        return action;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
