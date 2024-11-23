package com.n3.backend.dto.User;

public class UserRequest {
    private String email = "";
    private String fullname = "";
    private int page = 1;
    private int size = 10;
    private String sort = "id";
    private boolean isReverse = false;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
