package com.n3.backend.entities;

import com.n3.backend.dto.User;
import jakarta.persistence.*;

@Entity
public class CarOfUserEntity {
    @Id
    private int id;
    @OneToOne
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
