package com.n3.backend.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "tickets")
public class TicketEntity {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketTypeEntity ticketType;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
}
