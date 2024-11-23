package com.n3.backend.dto.ActionHistory;

import com.n3.backend.utils.DatetimeConvert;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.entities.ActionHistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class ActionHistory {
    private int id;
    private Car car;
    private String action;
    private String createdAt;

    public ActionHistory() {
    }

    public ActionHistory(ActionHistoryEntity actionHistoryEntity) {
        this.id = actionHistoryEntity.getId();
        this.car = new Car(actionHistoryEntity.getCar());
        this.action = actionHistoryEntity.getAction();
        this.createdAt = DatetimeConvert.timastampToString(actionHistoryEntity.getCreatedAt());
    }

    public static List<ActionHistory> convertList(List<ActionHistoryEntity> actionHistoryEntities) {
        List<ActionHistory> actionHistories = new ArrayList<>();
        for (ActionHistoryEntity actionHistoryEntity : actionHistoryEntities) {
            actionHistories.add(new ActionHistory(actionHistoryEntity));
        }
        return actionHistories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
