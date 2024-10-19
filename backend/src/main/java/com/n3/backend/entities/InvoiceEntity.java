package com.n3.backend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    private double total = 0;
    @CreationTimestamp
    private Date createdAt;
    private int status = 0;
}
