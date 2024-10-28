package com.n3.backend.dto.ActionHistory;

public class ActionHistoryRequest {
    private int id;
    private int carId;
    private String action;

    public ActionHistoryRequest() {
    }

    public ActionHistoryRequest(int id, int carId, String action) {
        this.id = id;
        this.carId = carId;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
