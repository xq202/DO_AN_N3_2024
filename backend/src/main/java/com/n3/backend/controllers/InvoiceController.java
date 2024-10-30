package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Invoice.Invoice;
import com.n3.backend.dto.Invoice.InvoiceRequest;
import com.n3.backend.dto.Invoice.InvoiceSearchRequest;
import com.n3.backend.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("")
    public ApiResponse<List<Invoice>> getAllInvoices(@ModelAttribute InvoiceSearchRequest request){
        return invoiceService.getAll(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<Invoice> getOneInvoice(@PathVariable int id){
        return invoiceService.getOneById(id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteInvoice(@PathVariable int id){
        return invoiceService.deleteItem(id);
    }

//    @PutMapping("/{id}")
//    public ApiResponse updateInvoice(@PathVariable int id, @RequestBody Invoice invoice){
//        return invoiceService.updateItem(id, invoice);
//    }

    @PostMapping("")
    public ApiResponse addNewInvoice(@RequestBody InvoiceRequest invoice){
        return new ApiResponse(true, 200, invoice, "success");
    }

    @GetMapping("{id}/active")
    public ApiResponse<Invoice> activeInvoice(@PathVariable int id){
        return invoiceService.activeInvoice(id);
    }
}
