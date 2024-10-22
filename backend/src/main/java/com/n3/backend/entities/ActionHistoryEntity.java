package com.n3.backend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "action_history")
public class ActionHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;
    String action;
    @CreationTimestamp
    @Column(name = "created_at")
    private String createdAt;
}
