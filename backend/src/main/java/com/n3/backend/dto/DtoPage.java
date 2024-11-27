package com.n3.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class DtoPage<T> {
    private int totalPage;
    private int currentPage;
    private int totalItem;

    private List<T> items;

    public DtoPage(int totalPage, int currentPage, int totalItem, List items) {
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.totalItem = totalItem;
        this.items = items;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
