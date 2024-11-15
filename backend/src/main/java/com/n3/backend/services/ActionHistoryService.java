package com.n3.backend.services;

import com.n3.backend.config.DatetimeConvert;
import com.n3.backend.dto.ActionHistory.ActionHistory;
import com.n3.backend.dto.ActionHistory.ActionHistoryRequest;
import com.n3.backend.dto.ActionHistory.ActionHistorySearchRequest;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.dto.User.User;
import com.n3.backend.entities.*;
import com.n3.backend.repositories.ActionHistoryRepository;
import com.n3.backend.repositories.CarRepository;
import com.n3.backend.repositories.PackingInfomationRepository;
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
    PackingInfomationRepository packingInfomationRepository;
    @Autowired
    UserService userService;

    public ApiResponse getAll(ActionHistorySearchRequest request){
        try{
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort()));

            Page data = repository.searchByCarCodeContainingIgnoreCaseAndActionContainingIgnoreCaseAndCreatedAtBetween(request.getCode(), request.getAction(), DatetimeConvert.stringToTimestamp(request.getStartDate()), DatetimeConvert.stringToTimestamp(request.getEndDate()), pageable);

            List<ActionHistoryEntity> list = data.stream().toList();
            int totalPage = data.getTotalPages();
            int totalItem = (int) data.getTotalElements();

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage()+1, totalItem, ActionHistory.convertList(list)), "success");
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

            // check action
            if(!request.getAction().equals("IN") && !request.getAction().equals("OUT")){
                return new ApiResponse(false, 400, null, "Action is invalid");
            }

            if(request.getAction().equals("IN")){
                if(packingInfomation.getTotalSlotAvailable() <= 0){
                    return new ApiResponse(false, 400, null, "No slot available");
                }

                packingInfomation.setTotalSlotAvailable(packingInfomation.getTotalSlotAvailable() - 1);
            }
            else {
                packingInfomation.setTotalSlotAvailable(packingInfomation.getTotalSlotAvailable() + 1);
            }

            ActionHistoryEntity actionHistoryEntity = new ActionHistoryEntity();

            actionHistoryEntity.setCar(carRepository.findByCode(request.getCode()));
            actionHistoryEntity.setAction(request.getAction());

            repository.save(actionHistoryEntity);

            packingInfomationRepository.save(packingInfomation);

            return new ApiResponse(true, 200, new ActionHistory(actionHistoryEntity), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }
}
