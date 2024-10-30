package com.n3.backend.dto.Invoice;

public class TicketTypeIdCarId {
    private int ticketTypeId;
    private int carId;

    public TicketTypeIdCarId(int ticketTypeId, int carId) {
        this.ticketTypeId = ticketTypeId;
        this.carId = carId;
    }

    public int getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(int ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
