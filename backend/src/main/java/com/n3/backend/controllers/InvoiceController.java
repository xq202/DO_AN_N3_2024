package com.n3.backend.controllers;

import com.n3.backend.dto.ActionHistory.ResponseAction;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.dto.Invoice.Invoice;
import com.n3.backend.dto.Invoice.InvoiceRequest;
import com.n3.backend.dto.Invoice.InvoiceSearchRequest;
import com.n3.backend.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("")
    public ApiResponse<DtoPage<Invoice>> getAllInvoices(@ModelAttribute InvoiceSearchRequest request){
        return invoiceService.getAll(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<Invoice> getOneInvoice(@PathVariable int id){
        return invoiceService.getOneById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteInvoice(@PathVariable int id){
        return invoiceService.deleteItem(id);
    }

//    @PutMapping("/{id}")
//    public ApiResponse updateInvoice(@PathVariable int id, @RequestBody Invoice invoice){
//        return invoiceService.updateItem(id, invoice);
//    }

    @PostMapping("")
    public ApiResponse<ResponseAction> addNewInvoice(@RequestBody InvoiceRequest invoice){
        return invoiceService.addInvoice(invoice);
    }

    @GetMapping("{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Invoice> activeInvoice(@PathVariable int id){
        return invoiceService.activeInvoice(id);
    }
}
