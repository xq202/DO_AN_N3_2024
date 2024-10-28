package com.n3.backend.services;

import com.n3.backend.config.DatetimeConvert;
import com.n3.backend.dto.*;
import com.n3.backend.dto.Ticket.Ticket;
import com.n3.backend.dto.Ticket.TicketRequest;
import com.n3.backend.entities.CarEntity;
import com.n3.backend.entities.TicketEntity;
import com.n3.backend.entities.TicketTypeEntity;
import com.n3.backend.repositories.CarRepository;
import com.n3.backend.repositories.TicketRepository;
import com.n3.backend.repositories.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TicketTypeRepository ticketTypeRepository;
    @Autowired
    CarRepository carRepository;

    public ApiResponse<Ticket> addNewTicket(TicketRequest request){
        try {
            TicketEntity ticketEntity = new TicketEntity();
            ticketEntity.setCar(carRepository.findById(request.getCarId()).get());
            ticketEntity.setTicketType(ticketTypeRepository.getOne(request.getTicketTypeId()));
            ticketEntity.setStartDate(DatetimeConvert.stringToDate(request.getStartDate()));
            ticketEntity.setEndDate(DatetimeConvert.stringToDate(request.getEndDate()));

            return new ApiResponse<>(true, 200, new Ticket(ticketRepository.save(ticketEntity)), "Ticket added successfully");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<Ticket> getTicketById(int id){
        try {
            TicketEntity ticketEntity = ticketRepository.findById(id).get();
            return new ApiResponse<>(true, 200, new Ticket(ticketEntity), "Ticket found");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<Ticket> updateTicket(int id, TicketRequest request){
        try {
            TicketEntity ticketEntity = ticketRepository.findById(id).get();
            ticketEntity.setCar(new CarEntity(request.getCarId()));
            ticketEntity.setTicketType(new TicketTypeEntity(request.getTicketTypeId()));
            ticketEntity.setStartDate(DatetimeConvert.stringToDate(request.getStartDate()));
            ticketEntity.setEndDate(DatetimeConvert.stringToDate(request.getEndDate()));
            ticketRepository.save(ticketEntity);
            return new ApiResponse<>(true, 200, new Ticket(ticketEntity), "Ticket updated successfully");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<Ticket> deleteTicket(int id){
        try {
            TicketEntity ticketEntity = ticketRepository.findById(id).get();
            ticketRepository.delete(ticketEntity);
            return new ApiResponse<>(true, 200, null, "Ticket deleted successfully");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<List<Ticket>> getAllTickets(){
        try {
            List<TicketEntity> tickets = ticketRepository.findAll();
            return new ApiResponse<>(true, 200, Ticket.getTickets(tickets), "Tickets found");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse<Ticket> getTicketByCarId(int carId){
        try {
            return new ApiResponse<>(true, 200, new Ticket(ticketRepository.findByCarId(carId)), "Tickets found");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

}
