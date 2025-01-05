package com.n3.backend.controllers;


import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.TicketType.TicketType;
import com.n3.backend.services.GateService;
import com.n3.backend.services.TicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {
    @Autowired
    private TicketTypeService ticketTypeService;
    @Autowired
    private GateService gateService;
    @GetMapping("/ticket-type")
    public ApiResponse<TicketType> getTicketType(){
        return ticketTypeService.getAll();
    }

    @GetMapping("/test")
    public String test(){
        return gateService.openGate();
    }
}
