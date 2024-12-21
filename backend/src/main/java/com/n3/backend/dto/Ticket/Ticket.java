package com.n3.backend.dto.Ticket;

import com.n3.backend.utils.DatetimeConvert;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.TicketType.TicketType;
import com.n3.backend.entities.TicketEntity;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private int id;
    private String name;
    private Car car;
    private TicketType ticketType;
    private double price;
    private String startDate;
    private String endDate;
    private String createdAt;
    private String updatedAt;

    public Ticket(TicketEntity ticketEntity) {
        if(ticketEntity == null) return;
        this.id = ticketEntity.getId();
        this.name = ticketEntity.getInvoiceDetail().getTicketType().getName();
        this.car = new Car(ticketEntity.getInvoiceDetail().getCar());
        this.ticketType = new TicketType(ticketEntity.getInvoiceDetail().getTicketType());
        this.price = ticketEntity.getInvoiceDetail().getPrice();
        this.startDate = DatetimeConvert.timastampToString(ticketEntity.getStartDate());
        this.endDate = DatetimeConvert.timastampToString(ticketEntity.getEndDate());
        this.createdAt = DatetimeConvert.timastampToString(ticketEntity.getCreatedAt());
        this.updatedAt = DatetimeConvert.timastampToString(ticketEntity.getUpdatedAt());
    }

    public static List<Ticket> getTickets(List<TicketEntity> tickets) {
        List<Ticket> ticketList = new ArrayList<>();
        for (TicketEntity ticket : tickets) {
            ticketList.add(new Ticket(ticket));
        }
        return ticketList;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
