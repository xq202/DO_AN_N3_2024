package com.n3.backend.config;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VNPayConfig {
    public String vnp_Version = "2.1.0";
    public String vnp_Command = "pay";
    public String url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public String vnp_TmnCode = "8DQKOFYS";
    public String vnp_HashSecret = "YSU923YJEJTKRHWJ42OVCV3NGNE0JWAU";
    public String password = "2002Addcmal";
    public String vnp_locale = "vn";
    public String vnp_CurrCode = "VND";
//    public int vnp_Amount = 0;
//    public String vnp_CreateDate = "";
    public String vnp_OrderType = "other";
//    public String vnp_ReturnUrl = "http://localhost:8080/vnpay_return";
//    public String vnp_TxnRef = "";
//    public String vnp_OrderInfo = "";
//    public String vnp_IpAddr = "";
//    public String vnp_ExpireDate = "";
//    public String vnp_SecureHash = "";

    public String getIpAddr(HttpServletRequest request) {
        if(request == null) {
            return "127.0.0.1";
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String hmacSHA512(String input, String secret) {
        try{
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");

            byte[] bytes = secret.getBytes();
            sha512_HMAC.init(new javax.crypto.spec.SecretKeySpec(bytes, "HmacSHA512"));

            byte[] hash = sha512_HMAC.doFinal(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashResult = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                hashResult.append(String.format("%02x", b));
            }
            return hashResult.toString();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String hashAllFields(Map vnp_Params) {
        try {
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            return hmacSHA512(hashData.toString(), vnp_HashSecret);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String buildQueryUrl(Map fields) {
        StringBuilder url = new StringBuilder();
        Set<String> keys = fields.keySet();
        for (String key : keys) {
            try {
                url.append(key);
                url.append('=');
                url.append(URLEncoder.encode(fields.get(key).toString(), StandardCharsets.UTF_8.toString()));
                url.append('&');
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        url.replace(url.length() - 1, url.length(), "");

        return url.toString();
    }
}
