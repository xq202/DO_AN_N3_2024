package com.n3.backend.services;

import com.n3.backend.dto.Car.CarPacking;
import com.n3.backend.dto.Car.CarSearchRequest;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.entities.CurrentPacking;
import com.n3.backend.entities.PackingInformation;
import com.n3.backend.handler.InfoWebSocketHandler;
import com.n3.backend.repositories.CurrentPackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrentPackingService {
    @Autowired
    private CurrentPackingRepository repository;
    @Autowired
    private PackingInformationService packingInformationService;
    @Autowired
    private InfoWebSocketHandler infoWebSocketHandler;
    public CurrentPacking findByCarId(int carId){
        return repository.findByCarId(carId);
    }

    DtoPage getAllCarPacking(CarSearchRequest request){
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), request.isReverse() ? Sort.by(Sort.Direction.DESC, request.getSort()) : Sort.by(Sort.Direction.ASC, request.getSort()));

        Page data = repository.findAllCarPacking(request.getCode(), request.getEmail(), pageable);

        List<CurrentPacking> listPacking = data.stream().toList();

        List<CarPacking> list = CarPacking.listCarPacking(listPacking);

        int totalPage = data.getTotalPages();
        int totalItem = (int) data.getTotalElements();

        return new DtoPage(totalPage, request.getPage(), totalItem, list);
    }

    public void actionCarOut(int carId)
    {
        System.out.println("actionCarOut");
        PackingInformation packingInformation = packingInformationService.getInformation();

        packingInformation.setTotalSlotAvailable(packingInformation.getTotalSlotAvailable() + 1);

        packingInformationService.save(packingInformation);

        CurrentPacking currentPacking = repository.findByCarId(carId);

        repository.delete(currentPacking);

        infoWebSocketHandler.sendSlotInfo();
    }
}
