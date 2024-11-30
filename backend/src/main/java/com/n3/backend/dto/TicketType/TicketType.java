package com.n3.backend.dto.TicketType;

import com.n3.backend.utils.DatetimeConvert;
import com.n3.backend.entities.TicketTypeEntity;

public class TicketType {
    private String name;
    private String type;
    private double price;
    private String createdAt;
    private String updatedAt;

    public TicketType(TicketTypeEntity ticketTypeEntity) {
        this.name = ticketTypeEntity.getName();
        this.type = ticketTypeEntity.getType();
        this.price = ticketTypeEntity.getPrice();
        this.createdAt = DatetimeConvert.timastampToString(ticketTypeEntity.getCreatedAt());
        this.updatedAt = DatetimeConvert.timastampToString(ticketTypeEntity.getUpdatedAt());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
