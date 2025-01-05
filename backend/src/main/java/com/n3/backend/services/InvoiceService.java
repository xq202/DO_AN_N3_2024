package com.n3.backend.services;

import com.n3.backend.config.Constants;
import com.n3.backend.dto.ActionHistory.ResponseAction;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.dto.Invoice.Invoice;
import com.n3.backend.dto.Invoice.InvoiceRequest;
import com.n3.backend.dto.Invoice.InvoiceSearchRequest;
import com.n3.backend.dto.Invoice.TicketTypeIdCarId;
import com.n3.backend.entities.*;
import com.n3.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private TicketTypeService ticketTypeService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private PackingInformationService packingInformationService;
    @Autowired
    private VNPayService vnpayService;
    @Autowired
    private CarService carService;
    @Autowired
    CurrentPackingService currentPackingService;
    @Autowired
    private GateService gateService;

    public ApiResponse getAll(InvoiceSearchRequest request){
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort()));

            Page<InvoiceEntity> invoiceEntities = invoiceRepository.searchByUserFullnameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCaseAndStatus(request.getFullname(), request.getEmail(), request.getCode(), request.getStatus(), pageable);

            List<Invoice> invoices = Invoice.getListInvoice(invoiceEntities.stream().toList());
            int totalPage = invoiceEntities.getTotalPages();
            int totalItem = (int) invoiceEntities.getTotalElements();

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, invoices), "success");
        } catch (Exception e) {
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public InvoiceEntity save(InvoiceEntity invoiceEntity){
        return invoiceRepository.save(invoiceEntity);
    }

    public ApiResponse getOneById(int id){
        try {
            InvoiceEntity invoiceEntity = invoiceRepository.getOne(id);
            Invoice invoice = new Invoice(invoiceEntity);
            return new ApiResponse(true, 200, invoice, "success");
        } catch(Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse deleteItem(int id){
        try {
            //xoa invoice detail
            List<InvoiceDetailEntity> invoiceDetailEntities = invoiceDetailRepository.findAllByInvoiceId(id);
            for (InvoiceDetailEntity invoiceDetailEntity : invoiceDetailEntities) {
                invoiceDetailRepository.delete(invoiceDetailEntity);
            }
            //xoa invoice
            InvoiceEntity invoiceEntity = invoiceRepository.getOne(id);
            invoiceRepository.delete(invoiceEntity);

            return new ApiResponse(true, 200, null, "delete success");
        } catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

//    public ApiResponse updateItem(int id, InvoiceRequest request){
//        try {
//            InvoiceEntity invoiceEntity = invoiceRepository.getOne(id);
//            invoiceEntity.setUser(userRepository.getOne(request.getUserId()));
//            invoiceEntity.setTotal(request.getTotal());
//            invoiceEntity.setStatus(request.getStatus());
//            invoiceRepository.save(invoiceEntity);
//            return new ApiResponse(true, 200, new Invoice(invoiceEntity), "update success");
//        } catch (Exception e){
//            return new ApiResponse(false, 400, null, e.getMessage());
//        }
//    }

    public ApiResponse addInvoice(InvoiceRequest request){
        try {
            // lay thong tin packing
            PackingInformation packingInformation = packingInformationService.getInformation();

            // khoi tao invoice
            InvoiceEntity invoiceEntity = new InvoiceEntity();

            //lay user hien tai
            UserEntity userEntity = userService.getCurrentUser();
            //kiem tra user co ton tai khong
            if(userEntity == null){
                return new ApiResponse(false, 400, null, "User not found");
            }

            //lay danh sach ticket type id va car id ma user mua
            List<TicketTypeIdCarId> products = request.getIds();

            // kiem tra so luong slot con trong
            if(packingInformation.getTotalSlotBooked() + products.size() > packingInformation.getMaxSlotBooked()){
                return new ApiResponse(false, 400, null, "No slot available");
            }

            //gan thong tin cho invoice
            invoiceEntity.setUser(userEntity);

            //luu invoice
            invoiceEntity = invoiceRepository.save(invoiceEntity);

            //kiem tra danh sach san pham co rong khong
            if(products.size() == 0){
                return new ApiResponse(false, 400, null, "Product list is empty");
            }

            double total = 0;
            List<InvoiceDetailEntity> list = new ArrayList<>();
            //duyet qua tung san pham
            for (TicketTypeIdCarId product : products) {
                int ticketTypeId = product.getTicketTypeId();
                int carId = product.getCarId();

                //kiem tra ticket type co ton tai khong
                if(!ticketService.existsById(ticketTypeId)){
                    return new ApiResponse(false, 400, null, "Ticket type not found");
                }
                TicketTypeEntity ticketTypeEntity = ticketTypeService.getOne(ticketTypeId);

                if(ticketTypeEntity.getType().equals("hour")){
                    return new ApiResponse(false, 400, null, "Ticket type not is hour when buy ticket");
                }


                //kiem tra car co ton tai khong
                CarEntity carEntity = carService.findById(carId);
                if(carEntity == null){
                    return new ApiResponse(false, 400, null, "Car not found");
                }

                // kiem tra quyen
                if(!userEntity.isAdmin()){
                    if(carEntity.getUser().getId() != userEntity.getId()){
                        return new ApiResponse(false, 400, null, "You don't have permission to buy ticket for this carId " + carEntity.getCode());
                    }
                }

                //tao invoice detail
                InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();

                //gan thong tin cho invoice detail
                invoiceDetailEntity.setInvoiceId(invoiceEntity.getId());
                invoiceDetailEntity.setCar(carEntity);
                invoiceDetailEntity.setTicketType(ticketTypeEntity);
                invoiceDetailEntity.setPrice(ticketTypeEntity.getPrice());
                total += ticketTypeEntity.getPrice();

                //luu lai invoice detail
                list.add(invoiceDetailEntity);
            }

            invoiceDetailRepository.saveAll(list);

            invoiceEntity.setTotal(total);
            invoiceEntity = invoiceRepository.save(invoiceEntity);

            if(!userEntity.isAdmin()){
                // thanh toan online thi luu slot truoc
                packingInformation.setTotalSlotBooked(packingInformation.getTotalSlotBooked() + products.size());
                packingInformation.setTotalSlotBookedAvailable(packingInformation.getTotalSlotBookedAvailable() + products.size());
                packingInformationService.save(packingInformation);

                String url = vnpayService.createLink(invoiceEntity.getTotal(), invoiceEntity.getCode(), Constants.FeUrl, invoiceEntity.getCode(), null);
                ResponseAction responseAction = new ResponseAction();
                responseAction.setUrl(url);
                responseAction.setAction("pay-online");
                return new ApiResponse(true, 200, responseAction, "Invoice added successfully");
            }

            ResponseAction responseAction = new ResponseAction();
            responseAction.setAction("pay-offline");
            responseAction.setUrl(null);

            return new ApiResponse(true, 200, responseAction, "Invoice added successfully");
        } catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public ApiResponse activeInvoice(int id){
        try {
            //lay thong tin user hien tai
            UserEntity currentUser = userService.getCurrentUser();

            // lay thong tin packing
            PackingInformation packingInformation = packingInformationService.getInformation();

            //kiem tra invoice co ton tai khong
            if(!invoiceRepository.existsById(id)){
                return new ApiResponse(false, 400, null, "Invoice not found");
            }

            // thong tin invoice
            InvoiceEntity invoiceEntity = invoiceRepository.getOne(id);

            //kiem tra invoice da duoc active chua
            if(invoiceEntity.getStatus() == 1){
                return new ApiResponse(false, 400, null, "Invoice already activated");
            }

            //lay danh sach invoice detail
            List<InvoiceDetailEntity> invoiceEntities = invoiceDetailRepository.findAllByInvoiceId(id);

            // kiem tra so luong slot con trong
            if(packingInformation.getTotalSlotBooked() + invoiceEntities.size() > packingInformation.getMaxSlotBooked()){
                return new ApiResponse(false, 400, null, "No slot available");
            }

            // cap nhat so luong slot da dat
            packingInformation.setTotalSlotBooked(packingInformation.getTotalSlotBooked() + invoiceEntities.size());
            packingInformation.setTotalSlotBookedAvailable(packingInformation.getTotalSlotBookedAvailable() + invoiceEntities.size());

            //kiem tra invoice co ton tai khong
            if(!invoiceRepository.existsById(id)){
                return new ApiResponse(false, 400, null, "Invoice not found");
            }

            // lay user dat invoice
//            UserEntity user = invoiceEntity.getUser();

            //lay so du user
//            double balance = user.getBalance();

            //khoi tao tong tien invoice
            double total = 0;
            //tinh tong tien invoice
//            for (InvoiceDetailEntity invoiceDetailEntity : invoiceEntities) {
//                total += invoiceDetailEntity.getPrice();
//            }
            //kiem tra so du co du de thanh toan khong
//            if(balance < total){
//                return new ApiResponse(false, 400, null, "Not enough balance");
//            }

            //luu lai so luong slot da dat
            packingInformationService.save(packingInformation);

            //duyet qua tung invoice detail
            for (InvoiceDetailEntity invoiceDetailEntity : invoiceEntities) {
                //khoi tao ticket
                TicketEntity ticketEntity = new TicketEntity();
                //gan thong tin cho ticket
                ticketEntity.setInvoiceDetail(invoiceDetailEntity);
                ticketEntity.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
                ticketEntity.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(invoiceDetailEntity.getTicketType().getDuration())));

                //cong tien vao tong tien invoice
                total += invoiceDetailEntity.getPrice();
                //luu lai ticket
                ticketService.save(ticketEntity);
            }
            //active invoice
            invoiceEntity.setStatus(1);

            //tru tien user
//            user.setBalance(balance - total);

            //luu lai user
//            userRepository.save(user);

            //gan tong tien cho invoice
            invoiceEntity.setTotal(total);

            //luu lai invoice
            invoiceRepository.save(invoiceEntity);

            return new ApiResponse(true, 200, new Invoice(invoiceEntity), "Invoice activated successfully");
        } catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public void activeOnlineByCode(String code, String errorCode){
        InvoiceEntity invoice = invoiceRepository.findByCode(code);
        if(invoice == null || invoice.getStatus() == 1){
            return ;
        }

        List<InvoiceDetailEntity> invoiceDetailEntityList = invoiceDetailRepository.findAllByInvoiceId(invoice.getId());

        if(errorCode.equals("00")){
            invoice.setStatus(1);

            for (InvoiceDetailEntity invoiceDetailEntity : invoiceDetailEntityList) {
                TicketEntity ticketEntity = new TicketEntity();
                ticketEntity.setInvoiceDetail(invoiceDetailEntity);
                Timestamp startDate;
                Timestamp endDate;
                if(ticketEntity.getInvoiceDetail().getTicketType().getType().equals("hour")){
                    startDate = invoice.getCreatedAt();
                    endDate = Timestamp.valueOf(LocalDateTime.now());

                    currentPackingService.actionCarOut(ticketEntity.getInvoiceDetail().getCar().getId());
                } else {
                    startDate = Timestamp.valueOf(LocalDateTime.now());
                    endDate = Timestamp.valueOf(LocalDateTime.now().plusDays(30));
                }
                ticketEntity.setStartDate(startDate);
                ticketEntity.setEndDate(endDate);
                ticketService.save(ticketEntity);
            }

            invoiceRepository.save(invoice);

            gateService.openGate();
        }
        else {
            PackingInformation packingInformation = packingInformationService.getInformation();
            packingInformation.setTotalSlotBooked(packingInformation.getTotalSlotBooked() - invoiceDetailEntityList.size());
            packingInformationService.save(packingInformation);
        }
    }
}
