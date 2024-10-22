package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.TicketTypeRequest;
import com.n3.backend.dto.TicketType;
import com.n3.backend.entities.TicketTypeEntity;
import com.n3.backend.repositories.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketTypeService {
    @Autowired
    private TicketTypeRepository repository;

    public ApiResponse<TicketType> addNewTicketType(TicketTypeRequest request){
        try {
            TicketTypeEntity ticketType = new TicketTypeEntity(0, request.getName(), request.getPrice());
            repository.save(ticketType);
            return new ApiResponse<TicketType>(true, 200, new TicketType(request.getName(), request.getPrice(), null, null), "success");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<TicketType> getOneById(int id){
        try {
            TicketTypeEntity ticketTypeEntity = repository.getOne(id);
            TicketType ticketType = new TicketType(ticketTypeEntity.getName(), ticketTypeEntity.getPrice(), ticketTypeEntity.getCreatedAt(), ticketTypeEntity.getUpdatedAt());
            return new ApiResponse<TicketType>(true, 200, ticketType, "success");
        }
        catch(Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse getAll(){
        try{
            List<TicketTypeEntity> list = repository.findAll();
            return new ApiResponse(true, 200, list, "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse deleteItem(int id){
        try {
            TicketTypeEntity ticketType = repository.getOne(id);
            repository.delete(ticketType);
            return new ApiResponse(true, 200, null, "delete success");
        }
        catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }
}
