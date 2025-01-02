package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.TicketType.TicketTypeRequest;
import com.n3.backend.dto.TicketType.TicketType;
import com.n3.backend.entities.TicketTypeEntity;
import com.n3.backend.repositories.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketTypeService {
    @Autowired
    private TicketTypeRepository repository;

    public ApiResponse<TicketType> addNewTicketType(TicketTypeRequest request){
        try {
            try{
                Double.valueOf(request.getPrice());
            }
            catch (Exception e){
                return new ApiResponse<>(false, 400, null, "Price must be a number");
            }

            TicketTypeEntity ticketType = new TicketTypeEntity(0, request.getName(), request.getPrice());
            repository.save(ticketType);
            return new ApiResponse<TicketType>(true, 200, new TicketType(ticketType), "success");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<TicketType> getOneById(int id){
        try {
            TicketTypeEntity ticketTypeEntity = repository.getOne(id);
            TicketType ticketType = new TicketType(ticketTypeEntity);
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

    public ApiResponse<TicketType> update(int id, TicketTypeRequest request){
        try {
            if(repository.existsById(id) == false){
                return new ApiResponse<>(false, 400, null, "Ticket type not found");
            }
            try{
                Double.valueOf(request.getPrice());
            }
            catch (Exception e){
                return new ApiResponse<>(false, 400, null, "Price must be a number");
            }
            TicketTypeEntity ticketType = repository.getOne(id);
            ticketType.setName(request.getName());
            ticketType.setPrice(request.getPrice());
            repository.save(ticketType);
            return new ApiResponse<TicketType>(true, 200, new TicketType(ticketType), "success");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public TicketTypeEntity getFirstByType(String type){
        return repository.findFirstByType(type);
    }

    public TicketTypeEntity getOne(int id){
        return repository.getOne(id);
    }
}
