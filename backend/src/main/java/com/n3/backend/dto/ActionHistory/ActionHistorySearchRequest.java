package com.n3.backend.dto.ActionHistory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ActionHistorySearchRequest {
    private String code = "";
    private String action = "";
    private int page = 1;
    private int size = 10;
    private boolean reverse;
    private String sort = "id";
    String startDate = (new Timestamp(0)).toString();
    String endDate = (Timestamp.valueOf(LocalDateTime.now())).toString();

    public ActionHistorySearchRequest() {
    }

    public ActionHistorySearchRequest(int page, int size, String code, String action, boolean reverse, String sort, String startDate, String endDate) {
        this.code = code;
        this.action = action;
        this.page = page;
        this.size = size;
        this.reverse = reverse;
        this.sort = sort;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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
}
