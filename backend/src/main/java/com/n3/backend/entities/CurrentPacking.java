package com.n3.backend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
public class CurrentPacking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @JoinTable(name = "cars", joinColumns = @JoinColumn(name = "id"))
    private int carId;

    @CreationTimestamp
    private Timestamp createdAt;

    public CurrentPacking(int id, int carId) {
        this.id = id;
        this.carId = carId;
    }

    public CurrentPacking() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
