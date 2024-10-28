package com.n3.backend.dto.Car;

import com.n3.backend.config.DatetimeConvert;
import com.n3.backend.entities.CarEntity;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private int id;
    private String name;
    private String code;
    private String createdAt;
    private String updatedAt;

    public Car(CarEntity carEntity) {
        this.id = carEntity.getId();
        this.name = carEntity.getName();
        this.code = carEntity.getCode();
        this.createdAt = DatetimeConvert.timastampToString(carEntity.getCreatedAt());
        this.updatedAt = DatetimeConvert.timastampToString(carEntity.getUpdatedAt());
    }

    public static List<Car> listCar(List<CarEntity> cars) {
        List<Car> carList = new ArrayList<>();
        for (CarEntity car : cars) {
            carList.add(new Car(car));
        }
        return carList;
    }
    public Car(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
