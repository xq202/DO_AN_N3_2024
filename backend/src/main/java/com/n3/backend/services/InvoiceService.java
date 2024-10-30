package com.n3.backend.services;

import com.n3.backend.config.DatetimeConvert;
import com.n3.backend.dto.ApiResponse;
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
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;
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

    public ApiResponse getAll(InvoiceSearchRequest request){
        try {
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort()));
            Page<InvoiceEntity> invoiceEntities = invoiceRepository.searchByUserFullnameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCaseAndStatus(request.getFullname(), request.getEmail(), request.getCode(), request.getStatus(), pageable);
            return new ApiResponse(true, 200, Invoice.getListInvoice(invoiceEntities.stream().toList()), "success");
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
            // khoi tao invoice
            InvoiceEntity invoiceEntity = new InvoiceEntity();

            //lay user hien tai
            UserEntity userEntity = userService.getCurrentUser();
            //kiem tra user co ton tai khong
            if(userEntity == null){
                return new ApiResponse(false, 400, null, "User not found");
            }
            //gan thong tin cho invoice
            invoiceEntity.setUser(userEntity);
            //luu invoice
            InvoiceEntity invoiceEntitySaved = invoiceRepository.save(invoiceEntity);
            //lay danh sach ticket type id va car id ma user mua
            List<TicketTypeIdCarId> products = request.getIds();

            //kiem tra danh sach san pham co rong khong
            if(products.size() == 0){
                return new ApiResponse(false, 400, null, "Product list is empty");
            }

            //duyet qua tung san pham
            for (TicketTypeIdCarId product : products) {
                int ticketTypeId = product.getTicketTypeId();
                int carId = product.getCarId();

                TicketTypeEntity ticketTypeEntity = ticketTypeRepository.getOne(ticketTypeId);
                //kiem tra ticket type co ton tai khong
                if(ticketTypeEntity == null){
                    return new ApiResponse(false, 400, null, "Ticket type not found");
                }

                CarEntity carEntity = carRepository.getOne(carId);
                //kiem tra car co ton tai khong
                if(carEntity == null){
                    return new ApiResponse(false, 400, null, "Car not found");
                }
                //tao invoice detail
                InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
                //gan thong tin cho invoice detail
                invoiceDetailEntity.setInvoiceId(invoiceEntitySaved.getId());
                invoiceDetailEntity.setCarId(carId);
                invoiceDetailEntity.setTicketTypeId(ticketTypeId);
                invoiceDetailEntity.setPrice(ticketTypeEntity.getPrice());
                //luu invoice detail
                invoiceDetailRepository.save(invoiceDetailEntity);
            }


            return new ApiResponse(true, 200, new Invoice(invoiceRepository.save(invoiceEntity)), "Invoice added successfully");
        } catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }

    public ApiResponse activeInvoice(int id){
        try {
            InvoiceEntity invoiceEntity = invoiceRepository.getOne(id);
            //kiem tra invoice co ton tai khong
            if(invoiceEntity == null){
                return new ApiResponse(false, 400, null, "Invoice not found");
            }
            //kiem tra invoice da duoc active chua
            if(invoiceEntity.getStatus() == 1){
                return new ApiResponse(false, 400, null, "Invoice already activated");
            }
            //active invoice
            invoiceEntity.setStatus(1);

            List<InvoiceDetailEntity> invoiceEntities = invoiceDetailRepository.findAllByInvoiceId(id);
            //khoi tao tong tien invoice
            double total = 0;

            for (InvoiceDetailEntity invoiceDetailEntity : invoiceEntities) {
                //khoi tao ticket
                TicketEntity ticketEntity = new TicketEntity();
                //gan thong tin cho ticket
                ticketEntity.setCar(carRepository.getOne(invoiceDetailEntity.getCarId()));
                TicketTypeEntity ticketTypeEntity = ticketTypeRepository.getOne(invoiceDetailEntity.getTicketTypeId());
                ticketEntity.setTicketType(ticketTypeEntity);
                ticketEntity.setStartDate(new Date(System.currentTimeMillis()));
                ticketEntity.setEndDate(new Date(System.currentTimeMillis() + ticketTypeEntity.getDuration() * 24 * 60 * 60 * 1000));

                //cong tien vao tong tien invoice
                total += invoiceDetailEntity.getPrice();
                //luu lai ticket
                TicketEntity ticketSaved =  ticketRepository.save(ticketEntity);
                //gan ticket id cho invoice detail
                invoiceDetailEntity.setTicketId(ticketSaved.getId());

                //luu lai invoice detail voi ticket id
                invoiceDetailRepository.save(invoiceDetailEntity);
            }
            //gan tong tien cho invoice
            invoiceEntity.setTotal(total);
            //luu lai invoice
            invoiceRepository.save(invoiceEntity);

            return new ApiResponse(true, 200, new Invoice(invoiceEntity), "Invoice activated successfully");
        } catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }


}
