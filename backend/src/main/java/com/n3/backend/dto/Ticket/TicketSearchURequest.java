package com.n3.backend.dto.Ticket;

import java.sql.Date;

public class TicketSearchURequest {
    private String code;
    private String startDate = (new Date(0)).toString() + " 00:00:00";
    private String endDate = (new Date(System.currentTimeMillis())).toString() + " 23:59:59";
    private int isExpired = -1;
    private int page = 1;
    private int size = 10;
    private String sort = "createdAt";
    private boolean isReverse = true;

    public int isExpired() {
        return isExpired;
    }

    public void setExpired(int expired) {
        isExpired = expired;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(int isExpired) {
        this.isExpired = isExpired;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }
}
