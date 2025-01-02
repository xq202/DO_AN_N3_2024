package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.Car.CarPacking;
import com.n3.backend.dto.Car.CarRequest;
import com.n3.backend.dto.Car.CarSearchRequest;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.dto.User.User;
import com.n3.backend.entities.CarEntity;
import com.n3.backend.entities.CurrentPacking;
import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.CarRepository;
import com.n3.backend.repositories.CurrentPackingRepository;
import com.n3.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    UserService userService;
    @Autowired
    CurrentPackingService currentPackingService;

    public ApiResponse getAll(CarSearchRequest request){
        try{
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), request.isReverse() ? Sort.by(Sort.Direction.DESC, request.getSort()) : Sort.by(Sort.Direction.ASC, request.getSort()));

            Page data =  repository.searchByUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCase(request.getEmail(), request.getCode(), pageable);

            List<CarEntity> list = data.stream().toList();

            int totalPage = data.getTotalPages();
            int totalItem = (int) data.getTotalElements();

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, Car.listCar(list)), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, "Error carService: " + e.getMessage());
        }
    }

    public ApiResponse getCarCurrentUser(CarSearchRequest request){
        try{
            UserEntity currentUser = userService.getCurrentUser();

            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), request.isReverse() ? Sort.by(Sort.Direction.DESC, request.getSort()) : Sort.by(Sort.Direction.ASC, request.getSort()));

            Page data = repository.searchByUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCaseAndUserIdAndIsDeleted(request.getEmail(), request.getCode(), currentUser.getId(),false, pageable);

            List<CarEntity> list = data.stream().toList();
            int totalPage = data.getTotalPages();
            int totalItem = (int) data.getTotalElements();

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, Car.listCar(list)), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, "Error carService: " + e.getMessage());
        }
    }

    public ApiResponse<Car> getOneById(int id){
        try {
            CarEntity carEntity = repository.findById(id).orElse(null);
            if (carEntity == null) {
                return new ApiResponse<>(false, 400, null, "Car is not exist");
            }
            UserEntity currentUser = userService.getCurrentUser();
            if(!currentUser.isAdmin()){
                if(currentUser.getId() != carEntity.getUser().getId()){
                    return new ApiResponse<>(false, 400, null, "You don't have permission to get car");
                }
            }

            return new ApiResponse(true, 200, new Car(carEntity), "Success");
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
                if(!userService.existsById(car.getUserId())){
                    return new ApiResponse<>(false, 400, null, "User is not exist");
                }
            }
            //check car code is exist
            if(findByCode(car.getCode().trim() ) != null){
                return new ApiResponse<>(false, 400, null, "Car is exist");
            }

            CarEntity carEntity = new CarEntity();
            carEntity.setName(car.getName());
            carEntity.setCode(car.getCode());

            if(currentUser.isAdmin()){
                carEntity.setUser(userService.getById(car.getUserId()));
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
            CarEntity carEntity = findById(id);

            if(carEntity == null){
                return new ApiResponse<>(false, 400, null, "Car is not exist");
            }

            if(carEntity.isDeleted()){
                return new ApiResponse<>(false, 400, null, "Car is deleted");
            }

            UserEntity currentUser = userService.getCurrentUser();

            if(!currentUser.isAdmin()){
                if(car.getUserId() >= 0 && currentUser.getId() != carEntity.getUser().getId()){
                    return new ApiResponse<>(false, 400, null, "You don't have permission to update car for other user");
                }
            }
            else {
                if(!userService.existsById(car.getUserId())){
                    return new ApiResponse<>(false, 400, null, "User is not exist");
                }
            }

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
            UserEntity currentUser = userService.getCurrentUser();

            CarEntity carEntity = findById(id);
            if(carEntity == null){
                return new ApiResponse(false, 400, null, "Car is not exist");
            }

            CurrentPacking currentPacking = currentPackingService.findByCarId(id);
            if(currentPacking != null){
                return new ApiResponse(false, 400, null, "Car is parking, can't delete");
            }

            if(!currentUser.isAdmin() && currentUser.getId() != carEntity.getUser().getId()){
                return new ApiResponse(false, 400, null, "You don't have permission to delete car");
            }

            carEntity.setDeleted(true);
            repository.save(carEntity);

            return new ApiResponse(true, 200, null, "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse getCarParking(CarSearchRequest request){
        try {
            DtoPage dtoPage = currentPackingService.getAllCarPacking(request);

            return new ApiResponse(true, 200, dtoPage, "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public CarEntity findByCode(String code){
        return repository.findByCodeAndIsDeleted(code, false);
    }

    public CarEntity findById(int id){
        return repository.findByIdAndIsDeleted(id, false);
    }

    public CarEntity findOneByIdAll(int id){
        return repository.findById(id).orElse(null);
    }
}
