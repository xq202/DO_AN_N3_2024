package com.n3.backend.services;

import com.n3.backend.config.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class VNPayService {
    public String createLink(double total, String vnp_OrderInfo, String vnp_returnUrl, String vnp_TxnRef, HttpServletRequest request) {
        VNPayConfig vnpayConfig = new VNPayConfig();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = dtf.format(LocalDateTime.now());
        String vnp_ExpireDate = dtf.format(LocalDateTime.now().plusMinutes(10));
        int vnp_Amount = (int)(total * 100);
        String vnp_IpAddr = vnpayConfig.getIpAddr(request);
        Map<String, String> fields = new HashMap<>();
        fields.put("vnp_Version", vnpayConfig.vnp_Version);
        fields.put("vnp_Command", vnpayConfig.vnp_Command);
        fields.put("vnp_TmnCode", vnpayConfig.vnp_TmnCode);
        fields.put("vnp_Amount", String.valueOf(vnp_Amount));
        fields.put("vnp_CurrCode", vnpayConfig.vnp_CurrCode);
        fields.put("vnp_CreateDate", vnp_CreateDate);
        fields.put("vnp_CurrCode", vnpayConfig.vnp_CurrCode);
        fields.put("vnp_IpAddr", vnp_IpAddr);
        fields.put("vnp_Locale", vnpayConfig.vnp_locale);
        fields.put("vnp_OrderInfo", vnp_OrderInfo);
        fields.put("vnp_OrderType", vnpayConfig.vnp_OrderType);
        fields.put("vnp_ReturnUrl", vnp_returnUrl);
        fields.put("vnp_ExpireDate", vnp_ExpireDate);
        fields.put("vnp_TxnRef", vnp_TxnRef);
        String vnp_SecureHash = vnpayConfig.hashAllFields(fields);
        fields.put("vnp_SecureHash", vnp_SecureHash);

        return vnpayConfig.url + "?" + vnpayConfig.buildQueryUrl(fields);
    }
}
