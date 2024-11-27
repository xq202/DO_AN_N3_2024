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
    @JoinColumn(name = "car_id")
    private CarEntity car;
    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketTypeEntity ticketType;
    @CreationTimestamp
    private Timestamp createdAt;

    public CurrentPacking(CarEntity car, TicketTypeEntity ticketType, Timestamp createdAt) {
        this.car = car;
        this.ticketType = ticketType;
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

    public TicketTypeEntity getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypeEntity ticketType) {
        this.ticketType = ticketType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
