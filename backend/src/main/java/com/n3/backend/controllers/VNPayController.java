package com.n3.backend.controllers;

import com.n3.backend.config.VNPayConfig;
import com.n3.backend.dto.VNPay.VnpResponse;
import com.n3.backend.services.InvoiceService;
import com.n3.backend.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
public class VNPayController {
    @Autowired
    private VNPayService vnpayService;
    @Autowired
    private InvoiceService invoiceService;
    @GetMapping("/IPN")
    public VnpResponse IPN(HttpServletRequest request){
        try {
            System.out.println("IPN");
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            VNPayConfig vnpayConfig = new VNPayConfig();
            // Check checksum
            String signValue = vnpayConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                invoiceService.activeOnlineByCode(fields.get("vnp_TxnRef").toString(), fields.get("vnp_ResponseCode").toString());

            } else {
                return new VnpResponse("97", "Invalid Checksum");
            }
            System.out.println("Confirm Success");
            return new VnpResponse("00", "Confirm Success");
        } catch (Exception e) {
            e.printStackTrace();
            return new VnpResponse("99", "Unknow error");
        }
    }
}
