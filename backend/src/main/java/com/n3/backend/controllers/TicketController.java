package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.dto.Ticket.Ticket;
import com.n3.backend.dto.Ticket.TicketRequest;
import com.n3.backend.dto.Ticket.TicketSearchRequest;
import com.n3.backend.dto.Ticket.TicketSearchURequest;
import com.n3.backend.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;
    @GetMapping("/{id}")
    public ApiResponse<Ticket> getOneById(@PathVariable("id") int id){
        return ticketService.getTicketById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ApiResponse<DtoPage<Ticket>> getAll(@ModelAttribute TicketSearchRequest request){
        return ticketService.getAllTickets(request);
    }

//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Ticket> update(@PathVariable("id") int id, @ModelAttribute TicketRequest request){
//        return ticketService.updateTicket(id, request);
//    }

//    @PostMapping("")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Ticket> addNew(@RequestBody TicketRequest request) throws SQLException {
//        return ticketService.addNewTicket(request);
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse delete(@PathVariable("id") int id){
        return ticketService.deleteTicket(id);
    }

    @GetMapping("/user")
    public ApiResponse<DtoPage<Ticket>> getByUser(@ModelAttribute TicketSearchURequest request){
        return ticketService.getTicketForUser(request);
    }
}
