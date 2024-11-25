package com.n3.backend.services;

import com.google.gson.Gson;
import com.n3.backend.dto.SlotInfo;
import com.n3.backend.entities.PackingInfomation;
import com.n3.backend.handler.InfoWebSocketHandler;
import com.n3.backend.repositories.PackingInfomationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoPackingService {
    @Autowired
    private PackingInfomationRepository packingInfomationRepository;
    public SlotInfo getSlotInfo(){
        PackingInfomation packingInfomation = packingInfomationRepository.findFirst();

        SlotInfo slotInfo = new SlotInfo(packingInfomation.getTotalSlot(), packingInfomation.getTotalSlotAvailable(), packingInfomation.getTotalSlotBooked(), packingInfomation.getTotalSlotBookedAvailable());

        return slotInfo;
    }
}
