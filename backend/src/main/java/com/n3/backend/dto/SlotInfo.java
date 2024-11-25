package com.n3.backend.dto;

public class SlotInfo {
    private int totalSlot;
    private int slotAvailable;
    private int totalSlotBooked;
    private int slotBookedAvailable;

    public SlotInfo(int totalSlot, int slotAvailable, int totalSlotBooked, int slotBookedAvailable) {
        this.totalSlot = totalSlot;
        this.slotAvailable = slotAvailable;
        this.totalSlotBooked = totalSlotBooked;
        this.slotBookedAvailable = slotBookedAvailable;
    }

    public int getTotalSlot() {
        return totalSlot;
    }

    public void setTotalSlot(int totalSlot) {
        this.totalSlot = totalSlot;
    }

    public int getSlotAvailable() {
        return slotAvailable;
    }

    public void setSlotAvailable(int slotAvailable) {
        this.slotAvailable = slotAvailable;
    }

    public int getTotalSlotBooked() {
        return totalSlotBooked;
    }

    public void setTotalSlotBooked(int totalSlotBooked) {
        this.totalSlotBooked = totalSlotBooked;
    }

    public int getSlotBookedAvailable() {
        return slotBookedAvailable;
    }

    public void setSlotBookedAvailable(int slotBookedAvailable) {
        this.slotBookedAvailable = slotBookedAvailable;
    }
}
