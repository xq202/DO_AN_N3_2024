package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.TicketType;
import com.n3.backend.dto.TicketTypeRequest;
import com.n3.backend.services.TicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ticket-type")
public class TicketTypeController {
    @Autowired
    TicketTypeService ticketTypeService;

    @GetMapping("/{id}")
    public ApiResponse<TicketType> getOneById(@PathVariable("id") int id){
        return ticketTypeService.getOneById(id);
    }

    @GetMapping("/")
    public ApiResponse getAll(){
        return ticketTypeService.getAll();
    }
    @PostMapping("/")
    public ApiResponse<TicketType> addNew(TicketTypeRequest request){
        return ticketTypeService.addNewTicketType(request);
    }
}
