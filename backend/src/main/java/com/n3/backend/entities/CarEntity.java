package com.n3.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class CarEntity {
    @Id
    private int id;
    private String name;
    @Column(nullable = false)
    private String code;

    public CarEntity(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public CarEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
