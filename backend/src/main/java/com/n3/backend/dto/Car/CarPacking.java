package com.n3.backend.dto.Car;

import com.n3.backend.dto.Ticket.Ticket;
import com.n3.backend.dto.TicketType.TicketType;
import com.n3.backend.entities.CurrentPacking;
import com.n3.backend.entities.TicketEntity;
import com.n3.backend.entities.TicketTypeEntity;
import com.n3.backend.utils.DatetimeConvert;

import java.util.List;

public class CarPacking {
    private Car car;
    private TicketType ticketType;
    private String createdAt;

    public CarPacking(Car car, TicketTypeEntity ticketType, String createdAt) {
        this.car = car;
        this.ticketType = new TicketType(ticketType);
        this.createdAt = createdAt;
    }

    public static List<CarPacking> listCarPacking(List<CurrentPacking> currentPackings){
        List<CarPacking> list = currentPackings.stream().map(
                currentPacking ->
                        new CarPacking(
                                new Car(currentPacking.getCar()),
                                currentPacking.getTicket().getInvoiceDetail().getTicketType(),
                                DatetimeConvert.timastampToString(currentPacking.getCreatedAt())
                        )
                ).toList();
        return list;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
