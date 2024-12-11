package com.n3.backend.services;

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
import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private TicketTypeRepository ticketTypeRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PackingInformationRepository packingInformationRepository;
    @Autowired
    VNPayService vnpayService;
    @Autowired
    ActionHistoryService actionHistoryService;

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
            PackingInformation packingInformation = packingInformationRepository.findFirst();

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
            //duyet qua tung san pham
            for (TicketTypeIdCarId product : products) {
                int ticketTypeId = product.getTicketTypeId();
                int carId = product.getCarId();

                //kiem tra ticket type co ton tai khong
                if(!ticketTypeRepository.existsById(ticketTypeId)){
                    return new ApiResponse(false, 400, null, "Ticket type not found");
                }
                TicketTypeEntity ticketTypeEntity = ticketTypeRepository.getOne(ticketTypeId);


                //kiem tra car co ton tai khong
                if(!carRepository.existsById(carId)){
                    return new ApiResponse(false, 400, null, "Car not found");
                }
                CarEntity carEntity = carRepository.getOne(carId);

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
                invoiceDetailEntity.setCarId(carId);
                invoiceDetailEntity.setTicketTypeId(ticketTypeId);
                invoiceDetailEntity.setPrice(ticketTypeEntity.getPrice());
                total += ticketTypeEntity.getPrice();

                //luu invoice detail
                invoiceDetailRepository.save(invoiceDetailEntity);
            }

            invoiceEntity.setTotal(total);
            invoiceEntity = invoiceRepository.save(invoiceEntity);

            if(!userEntity.isAdmin()){
                // thanh toan online thi luu slot truoc
                packingInformation.setTotalSlotBooked(packingInformation.getTotalSlotBooked() + products.size());
                packingInformationRepository.save(packingInformation);

                String url = vnpayService.createLink(invoiceEntity.getTotal(), invoiceEntity.getCode(), "http://localhost:8080/vnpay_return", invoiceEntity.getCode(), null);
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
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse activeInvoice(int id){
        try {
            //lay thong tin user hien tai
            UserEntity currentUser = userService.getCurrentUser();

            // lay thong tin packing
            PackingInformation packingInformation = packingInformationRepository.findFirst();

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
            for (InvoiceDetailEntity invoiceDetailEntity : invoiceEntities) {
                total += invoiceDetailEntity.getPrice();
            }
            //kiem tra so du co du de thanh toan khong
//            if(balance < total){
//                return new ApiResponse(false, 400, null, "Not enough balance");
//            }

            //luu lai so luong slot da dat
            packingInformationRepository.save(packingInformation);

            //duyet qua tung invoice detail
            for (InvoiceDetailEntity invoiceDetailEntity : invoiceEntities) {
                //khoi tao ticket
                TicketEntity ticketEntity = new TicketEntity();
                //gan thong tin cho ticket
                ticketEntity.setCar(carRepository.getOne(invoiceDetailEntity.getCarId()));
                TicketTypeEntity ticketTypeEntity = ticketTypeRepository.getOne(invoiceDetailEntity.getTicketTypeId());
                ticketEntity.setTicketType(ticketTypeEntity);
                ticketEntity.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
                ticketEntity.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(ticketTypeEntity.getDuration())));
                ticketEntity.setInvoiceId(invoiceEntity.getId());

                //cong tien vao tong tien invoice
                total += invoiceDetailEntity.getPrice();
                //luu lai ticket
                TicketEntity ticketSaved =  ticketRepository.save(ticketEntity);
                //gan ticket id cho invoice detail
                invoiceDetailEntity.setTicketId(ticketSaved.getId());

                //luu lai invoice detail voi ticket id
                invoiceDetailRepository.save(invoiceDetailEntity);
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
                ticketEntity.setCar(carRepository.getOne(invoiceDetailEntity.getCarId()));
                ticketEntity.setTicketType(ticketTypeRepository.getOne(invoiceDetailEntity.getTicketTypeId()));
                ticketEntity.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
                Timestamp startDate;
                Timestamp endDate;
                if(ticketEntity.getTicketType().getType().equals("hour")){
                    startDate = invoice.getCreatedAt();
                    endDate = Timestamp.valueOf(LocalDateTime.now());

                    actionHistoryService.actionCarOut(ticketEntity.getCar().getId());
                } else {
                    startDate = Timestamp.valueOf(LocalDateTime.now());
                    endDate = Timestamp.valueOf(LocalDateTime.now().plusDays(30));
                }
                ticketEntity.setStartDate(startDate);
                ticketEntity.setEndDate(endDate);
                ticketEntity.setInvoiceId(invoice.getId());
                ticketEntity.setPrice(invoiceDetailEntity.getPrice());
                ticketEntity = ticketRepository.save(ticketEntity);

                invoiceDetailEntity.setTicketId(ticketEntity.getId());
                invoiceDetailRepository.save(invoiceDetailEntity);
            }

            invoiceRepository.save(invoice);
        }
        else {
            PackingInformation packingInformation = packingInformationRepository.findFirst();
            packingInformation.setTotalSlotBooked(packingInformation.getTotalSlotBooked() - invoiceDetailEntityList.size());
            packingInformationRepository.save(packingInformation);
        }
    }
}
