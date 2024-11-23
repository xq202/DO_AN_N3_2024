package com.n3.backend.dto.Car;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CarSearchRequest {
    private String email = "";
    private String code = "";
    private int page = 1;
    private int size = 10;
    private boolean reverse;
    private String sort = "id";

    public CarSearchRequest() {
    }

    public CarSearchRequest(int page, int size, String email, String code, boolean reverse, String sort) {
        this.email = email;
        this.code = code;
        this.page = page;
        this.size = size;
        this.reverse = reverse;
        this.sort = sort;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
