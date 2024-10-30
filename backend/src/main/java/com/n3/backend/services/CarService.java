package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.Car.CarRequest;
import com.n3.backend.dto.Car.CarSearchRequest;
import com.n3.backend.dto.User.User;
import com.n3.backend.entities.CarEntity;
import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.CarRepository;
import com.n3.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    public ApiResponse<List<Car>> getAll(CarSearchRequest request){
        try{
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.isReverse() ? Sort.by(Sort.Direction.DESC, request.getSort()) : Sort.by(Sort.Direction.ASC, request.getSort()));
            List<CarEntity> list = repository.searchByUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCase(request.getEmail(), request.getCode(), pageable).stream().toList();
            return new ApiResponse(true, 200, Car.listCar(list), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, "Error carService: " + e.getMessage());
        }
    }

    public ApiResponse getCarCurrentUser(CarSearchRequest request){
        try{
            UserEntity currentUser = userService.getCurrentUser();
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.isReverse() ? Sort.by(Sort.Direction.DESC, request.getSort()) : Sort.by(Sort.Direction.ASC, request.getSort()));
            List<CarEntity> list = repository.searchByUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCaseAndId(request.getEmail(), request.getCode(), currentUser.getId(), pageable).stream().toList();
            return new ApiResponse(true, 200, Car.listCar(list), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, "Error carService: " + e.getMessage());
        }
    }

    public ApiResponse<Car> getOneById(int id){
        try {
            Car car = new Car(repository.getOne(id));
            return new ApiResponse<Car>(true, 200, car, "success");
        }
        catch(Exception e){
            return new ApiResponse<>(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse<Car> addNewCar(CarRequest car){
        try {
            UserEntity currentUser = userService.getCurrentUser();

            if(!currentUser.isAdmin()){
                if(car.getUserId() >= 0 && car.getUserId() != currentUser.getId()){
                    return new ApiResponse<>(false, 400, null, "You don't have permission to add car for other user");
                }
            }
            else {
                if(!userRepository.existsById(car.getUserId())){
                    return new ApiResponse<>(false, 400, null, "User is not exist");
                }
            }
            //check car code is exist
            if(repository.findByCode(car.getCode().trim() ) != null){
                return new ApiResponse<>(false, 400, null, "Car is exist");
            }

            CarEntity carEntity = new CarEntity();
            carEntity.setName(car.getName());
            carEntity.setCode(car.getCode());

            if(currentUser.isAdmin()){
                carEntity.setUser(userRepository.getOne(car.getUserId()));
            }
            else {
                carEntity.setUser(currentUser);
            }
            CarEntity carSaved =  repository.save(carEntity);

            return new ApiResponse(true, 200, new Car(carSaved), "success");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 500, null, e.getMessage());
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
            return new ApiResponse<>(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse deleteCar(int id){
        try {
            CarEntity carEntity = repository.getOne(id);
            repository.delete(carEntity);

            return new ApiResponse(true, 200, null, "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse<List<Car>> searchCar(String name){
        try {
            List<CarEntity> list = repository.findByNameContaining(name);
            return new ApiResponse(true, 200, Car.listCar(list), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse<List<Car>> searchCarByCode(String code){
        try {
            List<CarEntity> list = repository.findByCodeContaining(code);
            return new ApiResponse(true, 200, Car.listCar(list), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }
}
