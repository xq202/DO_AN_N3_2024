package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Ticket.Ticket;
import com.n3.backend.dto.Ticket.TicketRequest;
import com.n3.backend.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;
    @GetMapping("/{id}")
    public ApiResponse<Ticket> getOneById(@PathVariable("id") int id){
        return ticketService.getTicketById(id);
    }

    @GetMapping("/")
    public ApiResponse getAll(){
        return ticketService.getAllTickets();
    }

    @PutMapping("/{id}")
    public ApiResponse<Ticket> update(@PathVariable("id") int id, @ModelAttribute TicketRequest request){
        return ticketService.updateTicket(id, request);
    }

    @PostMapping("/")
    public ApiResponse<Ticket> addNew(@ModelAttribute TicketRequest request) throws SQLException {
        return ticketService.addNewTicket(request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable("id") int id){
        return ticketService.deleteTicket(id);
    }
}
