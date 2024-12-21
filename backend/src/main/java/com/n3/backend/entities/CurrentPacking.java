package com.n3.backend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
public class CurrentPacking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;
    @CreationTimestamp
    private Timestamp createdAt;

    public CurrentPacking(CarEntity car, TicketEntity ticket, Timestamp createdAt) {
        this.car = car;
        this.ticket = ticket;
        this.createdAt = createdAt;
    }

    public CurrentPacking() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CarEntity getCar() {
        return car;
    }

    public void setCar(CarEntity car) {
        this.car = car;
    }

    public TicketEntity getTicket() {
        return ticket;
    }

    public void setTicket(TicketEntity ticket) {
        this.ticket = ticket;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
