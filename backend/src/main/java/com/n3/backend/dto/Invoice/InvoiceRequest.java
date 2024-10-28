package com.n3.backend.dto.Invoice;

import java.util.List;
import java.util.Map;

public class InvoiceRequest {
    private int userId;
    private double total;
    private int status = 0;
    private Map<Integer, Integer> ticketIds;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<Integer, Integer> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(Map<Integer, Integer> ticketIds) {
        this.ticketIds = ticketIds;
    }
}
