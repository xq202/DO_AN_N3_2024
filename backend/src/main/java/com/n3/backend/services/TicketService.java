package com.n3.backend.services;

import com.n3.backend.utils.DatetimeConvert;
import com.n3.backend.dto.*;
import com.n3.backend.dto.Ticket.Ticket;
import com.n3.backend.dto.Ticket.TicketRequest;
import com.n3.backend.dto.Ticket.TicketSearchRequest;
import com.n3.backend.dto.Ticket.TicketSearchURequest;
import com.n3.backend.entities.CarEntity;
import com.n3.backend.entities.TicketEntity;
import com.n3.backend.entities.TicketTypeEntity;
import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.CarRepository;
import com.n3.backend.repositories.TicketRepository;
import com.n3.backend.repositories.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TicketTypeService ticketTypeService;
    @Autowired
    CarService carService;
    @Autowired
    UserService userService;

//    public ApiResponse<Ticket> addNewTicket(TicketRequest request){
//        try {
//            TicketEntity ticketEntity = new TicketEntity();
//
//            ticketEntity.setCar(carService.findById(request.getCarId()));
//            if(ticketEntity.getCar() == null){
//                return new ApiResponse<>(false, 404, null, "Car not found");
//            }
//
//            ticketEntity.setTicketType(ticketTypeRepository.getOne(request.getTicketTypeId()));
//            if (ticketEntity.getTicketType() == null){
//                return new ApiResponse<>(false, 404, null, "Ticket type not found");
//            }
//
//            ticketEntity.setStartDate(DatetimeConvert.stringToTimestamp(request.getStartDate()));
//            ticketEntity.setEndDate(DatetimeConvert.stringToTimestamp(request.getEndDate()));
//
//            return new ApiResponse<>(true, 200, new Ticket(ticketRepository.save(ticketEntity)), "Ticket added successfully");
//        }
//        catch (Exception e){
//            return new ApiResponse<>(false, 400, null, e.getMessage());
//        }
//    }

    public ApiResponse<Ticket> getTicketById(int id){
        try {
            if(ticketRepository.existsById(id) == false){
                return new ApiResponse<>(false, 404, null, "Ticket not found");
            }

            TicketEntity ticketEntity = ticketRepository.findById(id).get();

            if(ticketEntity.getInvoiceDetail().getCar().getUser().getId() != userService.getCurrentUser().getId() && !userService.getCurrentUser().isAdmin()){
                return new ApiResponse<>(false, 403, null, "You don't have permission to access this ticket");
            }


            return new ApiResponse<>(true, 200, new Ticket(ticketEntity), "Ticket found");
        }
        catch (Exception e){
            return new ApiResponse<>(false, 400, null, e.getMessage());
        }
    }

//    public ApiResponse<Ticket> updateTicket(int id, TicketRequest request){
//        try {
//            TicketEntity ticketEntity = ticketRepository.findById(id).get();
//            ticketEntity.setCar(new CarEntity(request.getCarId()));
//            ticketEntity.setTicketType(new TicketTypeEntity(request.getTicketTypeId()));
//            ticketEntity.setStartDate(DatetimeConvert.stringToTimestamp(request.getStartDate()));
//            ticketEntity.setEndDate(DatetimeConvert.stringToTimestamp(request.getEndDate()));
//            ticketRepository.save(ticketEntity);
//            return new ApiResponse<>(true, 200, new Ticket(ticketEntity), "Ticket updated successfully");
//        }
//        catch (Exception e){
//            return new ApiResponse<>(false, 400, null, e.getMessage());
//        }
//    }

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

    public ApiResponse getAllTickets(TicketSearchRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort()));

            Page data = ticketRepository.search(request.getCode(), request.getEmail(), request.getTicketTypeId(), DatetimeConvert.stringToTimestamp(request.getStartDate()), DatetimeConvert.stringToTimestamp(request.getEndDate()), request.getIsExpired(), pageable);

            List<TicketEntity> tickets = data.stream().toList();
            int totalPage = data.getTotalPages();
            int totalItem = (int) data.getTotalElements();
            List<Ticket> ticketList = Ticket.getTickets(tickets);

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, ticketList), "Tickets found");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ApiResponse<>(false, 400, null, "Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, 400, null, e.getMessage());

        }
    }
    public ApiResponse getTicketForUser(TicketSearchURequest request){
        try {
            System.out.println(request.getStartDate());
            System.out.println(request.getEndDate());
            UserEntity currentUser = userService.getCurrentUser();
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort()));

            List<TicketEntity> tickets;
            int totalPage;
            int totalItem;
            List<Ticket> ticketList;

            if(request.getIsExpired() > 0){
                Page data = ticketRepository.findByInvoiceDetailCarCodeContainingIgnoreCaseAndEndDateAfterAndCreatedAtBetweenAndInvoiceDetailCarUserId(request.getCode(), Timestamp.valueOf(LocalDateTime.now()), DatetimeConvert.stringToTimestamp(request.getStartDate()), DatetimeConvert.stringToTimestamp(request.getEndDate()), currentUser.getId(), pageable);
                tickets = data.stream().toList();
                totalPage = data.getTotalPages();
                totalItem = (int) data.getTotalElements();
                ticketList = Ticket.getTickets(tickets);
            }
            else{
                Page data = ticketRepository.findByInvoiceDetailCarCodeContainingIgnoreCaseAndCreatedAtBetweenAndInvoiceDetailCarUserId(request.getCode(), DatetimeConvert.stringToTimestamp(request.getStartDate()), DatetimeConvert.stringToTimestamp(request.getEndDate()), currentUser.getId(), pageable);
                tickets = data.stream().toList();
                totalPage = data.getTotalPages();
                totalItem = (int) data.getTotalElements();
                ticketList = Ticket.getTickets(tickets);
            }

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, ticketList), "Tickets found");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public TicketEntity getTicketAvailableOfCar(int carId){
        TicketEntity tickets = ticketRepository.findFirstByInvoiceDetailCarIdAndEndDateAfter(carId, Timestamp.valueOf(LocalDateTime.now()));

        return tickets;
    }

//    @Scheduled(cron = "0 0 0 * * *")
    public void autoCheckExpiredTicket(){}

    public boolean existsById(int ticketTypeId) {
        return ticketRepository.existsById(ticketTypeId);
    }

    public TicketEntity findById(int id){
        Optional<TicketEntity> ticketEntity = ticketRepository.findById(id);
        if(ticketEntity.isPresent()){
            return ticketEntity.get();
        }
        return null;
    }

    public TicketEntity save(TicketEntity ticketEntity) {
        return ticketRepository.save(ticketEntity);
    }
}
