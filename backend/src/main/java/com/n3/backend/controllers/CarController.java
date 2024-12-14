package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.Car.CarPacking;
import com.n3.backend.dto.Car.CarRequest;
import com.n3.backend.dto.Car.CarSearchRequest;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.entities.CurrentPacking;
import com.n3.backend.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/car")
@RestController
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DtoPage<Car>> getAll(@ModelAttribute CarSearchRequest request){
        return carService.getAll(request);
    }

    @GetMapping("/get-car-user")
    public ApiResponse<DtoPage<Car>> getCarUser(@ModelAttribute CarSearchRequest request){
        return carService.getCarCurrentUser(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<Car> getOneById(@PathVariable("id") int id){
        return carService.getOneById(id);
    }

    @PostMapping("")
//    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Car> addNewCar(@RequestBody CarRequest car){
        return carService.addNewCar(car);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Car> updateCar(@PathVariable("id") int id, @RequestBody CarRequest car){
        return carService.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteCar(@PathVariable("id") int id){
        return carService.deleteCar(id);
    }

    @GetMapping("/packing")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DtoPage<CarPacking>> getAllCarPacking(@ModelAttribute CarSearchRequest request){
        return carService.getCarParking(request);
    }
}
