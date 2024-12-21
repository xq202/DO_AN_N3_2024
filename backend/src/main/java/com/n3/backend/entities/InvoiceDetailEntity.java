package com.n3.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice_detail")
public class InvoiceDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int invoiceId;
    private double price;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;
    @ManyToOne
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketTypeEntity ticketType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}
