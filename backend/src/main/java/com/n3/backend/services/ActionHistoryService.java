package com.n3.backend.services;

import com.n3.backend.config.Constants;
import com.n3.backend.dto.ActionHistory.*;
import com.n3.backend.dto.Car.Car;
import com.n3.backend.dto.Car.CarSearchRequest;
import com.n3.backend.dto.Ticket.Ticket;
import com.n3.backend.handler.InfoWebSocketHandler;
import com.n3.backend.repositories.*;
import com.n3.backend.utils.DatetimeConvert;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.entities.*;
import com.n3.backend.utils.TimeUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ActionHistoryService {
    @Autowired
    private ActionHistoryRepository repository;
    @Autowired
    private CarService carService;
    @Autowired
    private PackingInformationService packingInformationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CurrentPackingRepository currentPackingRepository;
    @Autowired
    private InfoWebSocketHandler infoWebSocketHandler;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketTypeService ticketTypeService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private VNPayService vnpayService;
    @Autowired
    InvoiceDetailRepository invoiceDetailRepository;

    public ApiResponse getAll(ActionHistorySearchRequest request){
        try{
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort()));

            Page data = repository.searchByCarCodeContainingIgnoreCaseAndActionContainingIgnoreCaseAndCreatedAtBetween(request.getCode(), request.getAction(), DatetimeConvert.stringToTimestamp(request.getStartDate()), DatetimeConvert.stringToTimestamp(request.getEndDate()), pageable);

            List<ActionHistoryEntity> list = data.stream().toList();
            int totalPage = data.getTotalPages();
            int totalItem = (int) data.getTotalElements();

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, ActionHistory.convertList(list)), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse getByCar(int id, ActionHistoryOneCar request){
        try {
            CarEntity carEntity = carService.findById(id);

            if(carEntity == null){
                return new ApiResponse(false, 400, null, "Car not found");
            }

            // kiem tra quyen
            UserEntity user = userService.getCurrentUser();
            if(!user.isAdmin()){
                if(carEntity.getUser().getId() != user.getId()){
                    return new ApiResponse(false, 403, null, "You don't have permission to access this car");
                }
            }

            Page<ActionHistoryEntity> data = repository.searchAllByCarIdAndActionContainingIgnoreCaseAndCreatedAtBetween(
                    id,
                    request.getAction(),
                    DatetimeConvert.stringToTimestamp(request.getStartDate()),
                    DatetimeConvert.stringToTimestamp(request.getEndDate()),
                    PageRequest.of(request.getPage() - 1,
                            request.getSize(),
                            Sort.by(
                                    request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC,
                                    request.getSort())));

            List<ActionHistoryEntity> actionHistoryEntitys = data.stream().toList();
            int totalPage = data.getTotalPages();
            int totalItem = (int) data.getTotalElements();

            List<ActionHistory> actionHistorys = ActionHistory.convertList(actionHistoryEntitys);

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, actionHistorys), "success");
        }
        catch(Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse deleteItem(int id){
        try {
            ActionHistoryEntity actionHistoryEntity = repository.getOne(id);
            repository.delete(actionHistoryEntity);
            return new ApiResponse(true, 200, null, "delete success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse updateItem(int id, ActionHistoryRequest request){
        try {
            ActionHistoryEntity actionHistoryEntity = repository.getOne(id);
            actionHistoryEntity.setCar(carService.findByCode(request.getCode()));
            actionHistoryEntity.setAction(request.getAction());
            repository.save(actionHistoryEntity);
            return new ApiResponse(true, 200, new ActionHistory(actionHistoryEntity), "update success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse addActionHistory(ActionHistoryRequest request){
        try {
            PackingInformation packingInformation = packingInformationService.getInformation();

            try {
                // check action
                if (!request.getAction().equals("IN") && !request.getAction().equals("OUT")) {
                    return new ApiResponse(false, 400, null, "Action is invalid");
                }
            }
            catch (Exception e){
                return new ApiResponse(false, 400, null, "Action is invalid " + e.getMessage());
            }

            // check car
            CarEntity car = carService.findByCode(request.getCode());
            if(car == null){
                return new ApiResponse(false, 400, null, "Car not found");
            }

            // check car is in parking
            CurrentPacking currentPacking = currentPackingRepository.findByCarId(car.getId());

            if(request.getAction().equals("IN")){
                if(currentPackingRepository.existsByCarId(car.getId())){
                    return new ApiResponse(false, 400, null, "Car is in parking");
                }
            }
            else {
                if(!currentPackingRepository.existsByCarId(car.getId())){
                    return new ApiResponse(false, 400, null, "Car is not in parking");
                }
            }

            boolean isBooked = false;

            double price = 0;

            if(request.getAction().equals("IN")){
                TicketEntity ticket = ticketService.getTicketAvailableOfCar(car.getId());
                isBooked = ticket != null;

                if((isBooked ? packingInformation.getTotalSlotBookedAvailable() : packingInformation.getTotalSlotAvailable()) <= 0){
                    return new ApiResponse(false, 400, null, "No slot available");
                }

                if(!isBooked) packingInformation.setTotalSlotAvailable(packingInformation.getTotalSlotAvailable() - 1);
                else packingInformation.setTotalSlotBookedAvailable(packingInformation.getTotalSlotBookedAvailable() - 1);

                currentPacking = new CurrentPacking();
                currentPacking.setCar(car);
                currentPacking.setTicket(ticket);
                currentPackingRepository.save(currentPacking);

                ActionHistoryEntity actionHistoryEntity = new ActionHistoryEntity();

                actionHistoryEntity.setCar(car);
                actionHistoryEntity.setAction(request.getAction());

                actionHistoryEntity = repository.save(actionHistoryEntity);

                packingInformationService.save(packingInformation);

                infoWebSocketHandler.sendSlotInfo();

                ResponseAction responseAction = new ResponseAction();
                responseAction.setAction("OPEN");
                responseAction.setUrl(null);

                return new ApiResponse(true, 200, responseAction, "success");
            }
            else {
                isBooked = currentPacking.getTicket() != null;
                // action out
                ResponseAction responseAction = new ResponseAction();
                // tinh tien
                if(!isBooked){
                    Timestamp timeOut = Timestamp.valueOf(LocalDateTime.now());

                    if(currentPacking == null){
                        return new ApiResponse(false, 400, null, "Car not in parking");
                    }

                    double time = TimeUtil.minusTimestamp(currentPacking.getCreatedAt(), timeOut);
                    System.out.println("time: " + time);

                    TicketTypeEntity ticketType = ticketTypeService.getFirstByType("hour");
                    price = time * ticketType.getPrice();
                    System.out.println("price: " + price);

                    InvoiceEntity invoiceEntity = new InvoiceEntity();

                    invoiceEntity.setUser(car.getUser());

                    if(price < 10000) price = 10000;

                    invoiceEntity.setTotal(price);

                    invoiceEntity.setCreatedAt(currentPacking.getCreatedAt());

                    invoiceEntity = invoiceService.save(invoiceEntity);

                    InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
                    invoiceDetailEntity.setInvoiceId(invoiceEntity.getId());
                    invoiceDetailEntity.setCar(car);
                    invoiceDetailEntity.setPrice(price);
                    invoiceDetailEntity.setTicketType(ticketType);

                    invoiceDetailRepository.save(invoiceDetailEntity);

                    String url = vnpayService.createLink(price, invoiceEntity.getCode(), Constants.FeUrl, invoiceEntity.getCode(), null);

                    responseAction.setAction("PAY");
                    responseAction.setUrl(url);
                }
                else {
                    if(currentPacking.getTicket().getEndDate().getTime() < LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)){
                        double time = TimeUtil.minusTimestamp(currentPacking.getTicket().getEndDate(), Timestamp.valueOf(LocalDateTime.now()));

                        TicketTypeEntity ticketType = ticketTypeService.getFirstByType("hour");

                        price = time * ticketType.getPrice();

                        InvoiceEntity invoiceEntity = new InvoiceEntity();

                        invoiceEntity.setUser(car.getUser());

                        if(price < 10000) price = 10000;

                        invoiceEntity.setTotal(price);

                        invoiceEntity.setCreatedAt(currentPacking.getTicket().getEndDate());

                        invoiceService.save(invoiceEntity);

                        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();

                        invoiceDetailEntity.setInvoiceId(invoiceEntity.getId());

                        invoiceDetailEntity.setCar(car);

                        invoiceDetailEntity.setPrice(price);

                        invoiceDetailEntity.setTicketType(ticketType);

                        invoiceDetailRepository.save(invoiceDetailEntity);

                        String url = vnpayService.createLink(price, invoiceEntity.getCode(), Constants.FeUrl, invoiceEntity.getCode(), null);

                        responseAction.setAction("PAY");
                        responseAction.setUrl(url);
                    }
                    else{
                        // month ticket
                        // open gate
                        responseAction.setAction("OPEN");
                        responseAction.setUrl(null);

                        // neu la ve thang thi cap nhat slot, xoa current parking, gui socket
                        packingInformation.setTotalSlotBookedAvailable(packingInformation.getTotalSlotBookedAvailable() + 1);

                        packingInformationService.save(packingInformation);

                        currentPacking = currentPackingRepository.findByCarId(car.getId());

                        currentPackingRepository.delete(currentPacking);

                        infoWebSocketHandler.sendSlotInfo();
                    }
                }

                ActionHistoryEntity actionHistoryEntity = new ActionHistoryEntity();

                actionHistoryEntity.setCar(car);
                actionHistoryEntity.setAction(request.getAction());

                repository.save(actionHistoryEntity);

                return new ApiResponse(true, 200, responseAction, "success");
            }
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }
}
