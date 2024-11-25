package com.n3.backend.services;

import com.n3.backend.dto.SlotInfo;
import com.n3.backend.handler.InfoWebSocketHandler;
import com.n3.backend.repositories.*;
import com.n3.backend.utils.DatetimeConvert;
import com.n3.backend.dto.ActionHistory.ActionHistory;
import com.n3.backend.dto.ActionHistory.ActionHistoryRequest;
import com.n3.backend.dto.ActionHistory.ActionHistorySearchRequest;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.entities.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionHistoryService {
    @Autowired
    private ActionHistoryRepository repository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private PackingInfomationRepository packingInfomationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CurrentPackingRepository currentPackingRepository;
    @Autowired
    private InfoWebSocketHandler infoWebSocketHandler;
    @Autowired
    private TicketRepository ticketRepository;

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

    public ApiResponse getByCar(int id, HttpServletRequest request){
        try {
            // kiem tra quyen
            UserEntity user = userService.getCurrentUser();
            if(!user.isAdmin()){
                if(carRepository.findById(id).get().getUser().getId() != user.getId()){
                    return new ApiResponse(false, 403, null, "You don't have permission to access this car");
                }
            }

            int page = Integer.parseInt(request.getParameter("page")==null ? "0" : request.getParameter("page"));
            int size = Integer.parseInt(request.getParameter("size")==null ? "10" : request.getParameter("size"));
            String sort = request.getParameter("sort")==null ? "id" : request.getParameter("sort");
            String action = request.getParameter("action")==null ? "" : request.getParameter("action");
            boolean reverse = Boolean.parseBoolean(request.getParameter("reverse")==null ? "false" : request.getParameter("reverse"));

            Page data = repository.searchAllByCarIdAndActionContainingIgnoreCase(id, action, PageRequest.of(page, size, Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, sort)));

            List<ActionHistoryEntity> actionHistoryEntitys = data.stream().toList();
            int totalPage = data.getTotalPages();
            int totalItem = (int) data.getTotalElements();

            List<ActionHistory> actionHistorys = ActionHistory.convertList(actionHistoryEntitys);

            return new ApiResponse(true, 200, new DtoPage(totalPage, page+1, totalItem, actionHistorys), "success");
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
            PackingInfomation packingInfomation = packingInfomationRepository.findFirst();

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
            System.out.println(currentPackingRepository.existsByCarId(car.getId()));
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

            if(request.getAction().equals("IN")){
                if((isBooked ? packingInfomation.getTotalSlotBookedAvailable() : packingInfomation.getTotalSlotAvailable()) <= 0){
                    return new ApiResponse(false, 400, null, "No slot available");
                }

                if(!isBooked) packingInfomation.setTotalSlotAvailable(packingInfomation.getTotalSlotAvailable() - 1);
                else packingInfomation.setTotalSlotBookedAvailable(packingInfomation.getTotalSlotBookedAvailable() - 1);

                CurrentPacking currentPacking = new CurrentPacking();
                currentPacking.setCarId(car.getId());
                currentPackingRepository.save(currentPacking);
            }
            else {
                if(!isBooked) packingInfomation.setTotalSlotAvailable(packingInfomation.getTotalSlotAvailable() + 1);
                else packingInfomation.setTotalSlotBookedAvailable(packingInfomation.getTotalSlotBookedAvailable() + 1);

                CurrentPacking currentPacking = currentPackingRepository.findByCarId(car.getId());

                currentPackingRepository.delete(currentPacking);
            }

            ActionHistoryEntity actionHistoryEntity = new ActionHistoryEntity();

            actionHistoryEntity.setCar(car);
            actionHistoryEntity.setAction(request.getAction());

            repository.save(actionHistoryEntity);

            packingInfomationRepository.save(packingInfomation);

            infoWebSocketHandler.sendSlotInfo();


            return new ApiResponse(true, 200, new ActionHistory(actionHistoryEntity), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }
}
