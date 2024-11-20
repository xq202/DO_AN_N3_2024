package com.n3.backend.dto.Statistics;

import java.util.List;

public class IncomeDto {
    private String ticketName;
    private int total;
    private double totalIncome;
    private List<Income> incomeList;

    public IncomeDto(String ticketName, int total, double totalIncome, List<Income> incomeList) {
        this.ticketName = ticketName;
        this.total = total;
        this.totalIncome = totalIncome;
        this.incomeList = incomeList;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Income> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<Income> incomeList) {
        this.incomeList = incomeList;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
