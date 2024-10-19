package com.n3.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice_detail")
public class InvoiceDetailEntity {
    @Id
    private int id;
    @OneToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    private TicketEntity ticket;
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;
}
