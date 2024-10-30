package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.TicketType.TicketType;
import com.n3.backend.dto.TicketType.TicketTypeRequest;
import com.n3.backend.services.TicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket-type")
public class TicketTypeController {
    @Autowired
    TicketTypeService ticketTypeService;

    @GetMapping("/{id}")
    public ApiResponse<TicketType> getOneById(@PathVariable("id") int id){
        return ticketTypeService.getOneById(id);
    }

    @GetMapping("")
    public ApiResponse<List<TicketType>> getAll(){
        return ticketTypeService.getAll();
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TicketType> addNew(@RequestBody TicketTypeRequest request){
        return ticketTypeService.addNewTicketType(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TicketType> update(@PathVariable("id") int id, TicketTypeRequest request){
        return ticketTypeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse delete(@PathVariable("id") int id){
        return ticketTypeService.deleteItem(id);
    }
}
