package com.n3.backend.services;

import com.n3.backend.config.DatetimeConvert;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Invoice.Invoice;
import com.n3.backend.dto.Invoice.InvoiceRequest;
import com.n3.backend.dto.Invoice.InvoiceSearchRequest;
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

    public ApiResponse getAll(InvoiceSearchRequest request){
        try {
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort()));
            Page<InvoiceEntity> invoiceEntities = invoiceRepository.searchByUserFullnameContainsAndUserEmailContainsAndIdContains(request.getFullname(), request.getEmail(), request.getId(), pageable);
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
            InvoiceEntity invoiceEntity = new InvoiceEntity();

            UserEntity userEntity = userRepository.getOne(request.getUserId());
            if(userEntity == null){
                return new ApiResponse(false, 400, null, "User not found");
            }

            invoiceEntity.setUser(userEntity);
            invoiceEntity.setTotal(request.getTotal());
            invoiceEntity.setStatus(request.getStatus());
            InvoiceEntity invoiceEntitySaved = invoiceRepository.save(invoiceEntity);

            Map<Integer, Integer> products = request.getTicketIds();
            for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
                int ticketTypeId = entry.getKey();
                int carId = entry.getValue();

                TicketTypeEntity ticketTypeEntity = ticketTypeRepository.getOne(ticketTypeId);
                if(ticketTypeEntity == null){
                    return new ApiResponse(false, 400, null, "Ticket type not found");
                }

                CarEntity carEntity = carRepository.getOne(carId);
                if(carEntity == null){
                    return new ApiResponse(false, 400, null, "Car not found");
                }

                InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
                invoiceDetailEntity.setInvoiceId(invoiceEntitySaved.getId());
                invoiceDetailEntity.setTicketId(entry.getKey());
                invoiceDetailEntity.setCarId(entry.getValue());
                invoiceDetailEntity.setTicketTypeId(entry.getKey());
                invoiceDetailEntity.setPrice(ticketTypeEntity.getPrice());

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
            invoiceEntity.setStatus(1);

            List<InvoiceDetailEntity> invoiceEntities = invoiceDetailRepository.findAllByInvoiceId(id);

            double total = 0;

            for (InvoiceDetailEntity invoiceDetailEntity : invoiceEntities) {
                TicketEntity ticketEntity = new TicketEntity();
                ticketEntity.setCar(carRepository.getOne(invoiceDetailEntity.getCarId()));
                TicketTypeEntity ticketTypeEntity = ticketTypeRepository.getOne(invoiceDetailEntity.getTicketTypeId());
                ticketEntity.setTicketType(ticketTypeEntity);
                ticketEntity.setStartDate(new Date(System.currentTimeMillis()));
                ticketEntity.setEndDate(new Date(System.currentTimeMillis() + ticketTypeEntity.getDuration() * 24 * 60 * 60 * 1000));

                total += ticketTypeEntity.getPrice();

                ticketRepository.save(ticketEntity);
            }

            invoiceEntity.setTotal(total);

            invoiceRepository.save(invoiceEntity);

            return new ApiResponse(true, 200, new Invoice(invoiceEntity), "Invoice activated successfully");
        } catch (Exception e){
            return new ApiResponse(false, 400, null, e.getMessage());
        }
    }


}
