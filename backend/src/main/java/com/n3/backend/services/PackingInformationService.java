package com.n3.backend.services;

import com.n3.backend.dto.SlotInfo;
import com.n3.backend.entities.PackingInformation;
import com.n3.backend.repositories.PackingInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackingInformationService {
    @Autowired
    private PackingInformationRepository packingInformationRepository;
    public SlotInfo getSlotInfo(){
        PackingInformation packingInformation = packingInformationRepository.findFirst();

        SlotInfo slotInfo = new SlotInfo(packingInformation.getTotalSlot(), packingInformation.getTotalSlotAvailable(), packingInformation.getTotalSlotBooked(), packingInformation.getTotalSlotBookedAvailable());

        return slotInfo;
    }

    public PackingInformation getInformation(){
        return packingInformationRepository.findFirst();
    }

    public PackingInformation save(PackingInformation packingInformation){
        return packingInformationRepository.save(packingInformation);
    }
}
