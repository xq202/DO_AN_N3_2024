package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.Car.CarRequest;
import com.n3.backend.dto.Car.CarSearchRequest;
import com.n3.backend.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/car")
@RestController
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("")
    public ApiResponse<List<Car>> getAll(@ModelAttribute CarSearchRequest request){
        return carService.getAll(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<Car> getOneById(@PathVariable("id") int id){
        return carService.getOneById(id);
    }

    @PostMapping("")
    public ApiResponse<Car> addNewCar(@RequestBody CarRequest car){
        return carService.addNewCar(car);
    }

    @PutMapping("/{id}")
    public ApiResponse<Car> updateCar(@PathVariable("id") int id, @RequestBody CarRequest car){
        return carService.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteCar(@PathVariable("id") int id){
        return carService.deleteCar(id);
    }
}
