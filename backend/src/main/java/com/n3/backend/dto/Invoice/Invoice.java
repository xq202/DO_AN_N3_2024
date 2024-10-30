package com.n3.backend.dto.Invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.n3.backend.config.DatetimeConvert;
import com.n3.backend.dto.Ticket.Ticket;
import com.n3.backend.entities.InvoiceEntity;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int id;
    private String code;
    private int userId;
    private double total = 0;
    private String createdAt;
    private String updatedAt;
    private int status = 0;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Invoice(InvoiceEntity invoiceEntity) {
        this.id = invoiceEntity.getId();
        this.userId = invoiceEntity.getUser().getId();
        this.total = invoiceEntity.getTotal();
        this.code = invoiceEntity.getCode();
        this.createdAt = DatetimeConvert.timastampToString(invoiceEntity.getCreatedAt());
        this.updatedAt = DatetimeConvert.timastampToString(invoiceEntity.getUpdatedAt());
        this.status = invoiceEntity.getStatus();
    }

    public static List<Invoice> getListInvoice(List<InvoiceEntity> invoiceEntities) {
        List<Invoice> invoices = new ArrayList<>();
        for(InvoiceEntity invoiceEntity : invoiceEntities) {
            invoices.add(new Invoice(invoiceEntity));
        }
        return invoices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
