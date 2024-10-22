package com.n3.backend.dto;

public class ApiResponse<T> {
    private boolean status;
    private int responseCode;
    private String message;
    private T data;

    public ApiResponse(boolean status, int responseCode, T data, String message) {
        this.status = status;
        this.responseCode = responseCode;
        this.data = data;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
