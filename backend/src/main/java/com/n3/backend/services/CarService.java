package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.Car.CarRequest;
import com.n3.backend.dto.Car.CarSearchRequest;
import com.n3.backend.entities.CarEntity;
import com.n3.backend.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository repository;

    public ApiResponse<List<Car>> getAll(CarSearchRequest request){
        try{
            List<CarEntity> list = repository.searchByNameContainsAndAndCodeContaining(request.getName(), request.getCode(), request.getPageable()).stream().toList();
            return new ApiResponse<>(true, 200, Car.listCar(list), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse<Car> getOneById(int id){
        try {
            Car car = new Car(repository.getOne(id));
            return new ApiResponse<Car>(true, 200, car, "success");
        }
        catch(Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<Car> addNewCar(CarRequest car){
        try {
            CarEntity carEntity = new CarEntity();
            carEntity.setName(car.getName());
            carEntity.setCode(car.getCode());
            repository.save(carEntity);

            return new ApiResponse<Car>(true, 200, new Car(carEntity), "success");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<Car> updateCar(int id, CarRequest car){
        try {
            CarEntity carEntity = repository.getOne(id);
            carEntity.setName(car.getName());
            carEntity.setCode(car.getCode());
            repository.save(carEntity);

            return new ApiResponse<Car>(true, 200, new Car(carEntity), "success");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse deleteCar(int id){
        try {
            CarEntity carEntity = repository.getOne(id);
            repository.delete(carEntity);

            return new ApiResponse(true, 200, null, "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<List<Car>> searchCar(String name){
        try {
            List<CarEntity> list = repository.findByNameContaining(name);
            return new ApiResponse(true, 200, Car.listCar(list), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<List<Car>> searchCarByCode(String code){
        try {
            List<CarEntity> list = repository.findByCodeContaining(code);
            return new ApiResponse(true, 200, Car.listCar(list), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }
}
