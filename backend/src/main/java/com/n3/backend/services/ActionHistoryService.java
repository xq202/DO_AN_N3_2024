package com.n3.backend.services;

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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ActionHistoryService {
    @Autowired
    private ActionHistoryRepository repository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private PackingInformationRepository packingInformationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CurrentPackingRepository currentPackingRepository;
    @Autowired
    private InfoWebSocketHandler infoWebSocketHandler;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;

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
            if(carRepository.existsById(id) == false){
                return new ApiResponse(false, 400, null, "Car not found");
            }

            CarEntity carEntity = carRepository.findById(id).get();
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
            actionHistoryEntity.setCar(carRepository.findByCode(request.getCode()));
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
            PackingInformation packingInformation = packingInformationRepository.findFirst();

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
            CarEntity car = carRepository.findByCode(request.getCode());
            if(car == null){
                return new ApiResponse(false, 400, null, "Car not found");
            }

            // check car is in parking
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
            TicketEntity ticketOfCar = ticketRepository.findFirstByCarIdAndIsExpired(car.getId(), false);

            if(ticketOfCar != null){
                isBooked = true;
            }

            double price = 0;

            if(request.getAction().equals("IN")){
                if((isBooked ? packingInformation.getTotalSlotBookedAvailable() : packingInformation.getTotalSlotAvailable()) <= 0){
                    return new ApiResponse(false, 400, null, "No slot available");
                }

                if(!isBooked) packingInformation.setTotalSlotAvailable(packingInformation.getTotalSlotAvailable() - 1);
                else packingInformation.setTotalSlotBookedAvailable(packingInformation.getTotalSlotBookedAvailable() - 1);

                CurrentPacking currentPacking = new CurrentPacking();
                currentPacking.setCar(car);
                currentPacking.setTicketType(ticketOfCar == null ? null : ticketOfCar.getTicketType());
                currentPackingRepository.save(currentPacking);
            }
            else {
                // tinh tien
                if(!isBooked){
                    ActionHistoryEntity lastActionIN = repository.findTopByActionAndCarIdOrderByCreatedAtDesc("IN", car.getId());
                    System.out.println(lastActionIN.getCreatedAt());
                    Timestamp timeOut = new Timestamp(System.currentTimeMillis());
                    System.out.println(timeOut);

                    if(lastActionIN == null){
                        return new ApiResponse(false, 400, null, "Last in is null");
                    }

                    double time = TimeUtil.minusTimestamp(lastActionIN.getCreatedAt(), timeOut);
                    System.out.println("time: " + time);

                    price = time * packingInformation.getPricePerHour();
                    System.out.println("price: " + price);

                    if(car.getUser().getBalance() < price){
                        return new ApiResponse(false, 400, null, "Not enough money");
                    }

                    car.getUser().setBalance(car.getUser().getBalance() - price);

                    userRepository.save(car.getUser());
                }

                if(!isBooked) packingInformation.setTotalSlotAvailable(packingInformation.getTotalSlotAvailable() + 1);
                else packingInformation.setTotalSlotBookedAvailable(packingInformation.getTotalSlotBookedAvailable() + 1);

                CurrentPacking currentPacking = currentPackingRepository.findByCarId(car.getId());

                currentPackingRepository.delete(currentPacking);
            }

            ActionHistoryEntity actionHistoryEntity = new ActionHistoryEntity();

            actionHistoryEntity.setCar(car);
            actionHistoryEntity.setAction(request.getAction());

            repository.save(actionHistoryEntity);

            packingInformationRepository.save(packingInformation);

            infoWebSocketHandler.sendSlotInfo();


            return new ApiResponse(true,
                    200,
                    new ActionHistoryOut(new Car(car),
                            actionHistoryEntity.getAction(),
                            DatetimeConvert.timastampToString(actionHistoryEntity.getCreatedAt()),
                            price,
                            ticketOfCar==null ? null : new Ticket(ticketOfCar)),
                    "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }
}
