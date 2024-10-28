package com.n3.backend.services;

import com.n3.backend.dto.ActionHistory.ActionHistory;
import com.n3.backend.dto.ActionHistory.ActionHistoryRequest;
import com.n3.backend.dto.ActionHistory.ActionHistorySearchRequest;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.entities.ActionHistoryEntity;
import com.n3.backend.repositories.ActionHistoryRepository;
import com.n3.backend.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionHistoryService {
    @Autowired
    private ActionHistoryRepository repository;
    @Autowired
    private CarRepository carRepository;

    public ApiResponse getAll(ActionHistorySearchRequest request){
        try{
            List<ActionHistoryEntity> actionHistoryEntity = repository.searchByCarCodeContainsAndAction(request.getCode(), request.getAction(), request.getPageable()).stream().toList();

            return new ApiResponse(true, 200, ActionHistory.convertList(actionHistoryEntity), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse getOneById(int id){
        try {
            ActionHistoryEntity actionHistoryEntity = repository.getOne(id);
            ActionHistory actionHistory = new ActionHistory(actionHistoryEntity);
            return new ApiResponse(true, 200, actionHistory, "success");
        }
        catch(Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse deleteItem(int id){
        try {
            ActionHistoryEntity actionHistoryEntity = repository.getOne(id);
            repository.delete(actionHistoryEntity);
            return new ApiResponse(true, 200, null, "delete success");
        }
        catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse updateItem(int id, ActionHistoryRequest request){
        try {
            ActionHistoryEntity actionHistoryEntity = repository.getOne(id);
            actionHistoryEntity.setCar(carRepository.getOne(request.getCarId()));
            actionHistoryEntity.setAction(request.getAction());
            repository.save(actionHistoryEntity);
            return new ApiResponse(true, 200, new ActionHistory(actionHistoryEntity), "update success");
        }
        catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse addActionHistory(ActionHistoryRequest request){
        try {
            ActionHistoryEntity actionHistoryEntity = new ActionHistoryEntity();
            actionHistoryEntity.setCar(carRepository.getOne(request.getCarId()));
            actionHistoryEntity.setAction(request.getAction());
            repository.save(actionHistoryEntity);
            return new ApiResponse(true, 200, new ActionHistory(actionHistoryEntity), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }
}
