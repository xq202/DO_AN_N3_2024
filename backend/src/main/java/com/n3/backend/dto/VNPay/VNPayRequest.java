package com.n3.backend.dto.VNPay;

import java.util.HashMap;
import java.util.Map;

public class VNPayRequest {
    private String vnp_Code;
    private String vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_PayDate;
    private String vnp_OrderInfo;
    private String vnp_TransactionNo;
    private String vnp_ResponseCode;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHashType;
    private String vnp_SecureHash;

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("vnp_Code", vnp_Code);
        map.put("vnp_Amount", vnp_Amount);
        map.put("vnp_BankCode", vnp_BankCode);
        map.put("vnp_BankTranNo", vnp_BankTranNo);
        map.put("vnp_CardType", vnp_CardType);
        map.put("vnp_PayDate", vnp_PayDate);
        map.put("vnp_OrderInfo", vnp_OrderInfo);
        map.put("vnp_TransactionNo", vnp_TransactionNo);
        map.put("vnp_ResponseCode", vnp_ResponseCode);
        map.put("vnp_TransactionStatus", vnp_TransactionStatus);
        map.put("vnp_TxnRef", vnp_TxnRef);
//        map.put("vnp_SecureHashType", vnp_SecureHashType);
//        map.put("vnp_SecureHash", vnp_SecureHash);
        return map;
    }

    public String getVnp_Code() {
        return vnp_Code;
    }

    public void setVnp_Code(String vnp_Code) {
        this.vnp_Code = vnp_Code;
    }

    public String getVnp_Amount() {
        return vnp_Amount;
    }

    public void setVnp_Amount(String vnp_Amount) {
        this.vnp_Amount = vnp_Amount;
    }

    public String getVnp_BankCode() {
        return vnp_BankCode;
    }

    public void setVnp_BankCode(String vnp_BankCode) {
        this.vnp_BankCode = vnp_BankCode;
    }

    public String getVnp_BankTranNo() {
        return vnp_BankTranNo;
    }

    public void setVnp_BankTranNo(String vnp_BankTranNo) {
        this.vnp_BankTranNo = vnp_BankTranNo;
    }

    public String getVnp_CardType() {
        return vnp_CardType;
    }

    public void setVnp_CardType(String vnp_CardType) {
        this.vnp_CardType = vnp_CardType;
    }

    public String getVnp_PayDate() {
        return vnp_PayDate;
    }

    public void setVnp_PayDate(String vnp_PayDate) {
        this.vnp_PayDate = vnp_PayDate;
    }

    public String getVnp_OrderInfo() {
        return vnp_OrderInfo;
    }

    public void setVnp_OrderInfo(String vnp_OrderInfo) {
        this.vnp_OrderInfo = vnp_OrderInfo;
    }

    public String getVnp_TransactionNo() {
        return vnp_TransactionNo;
    }

    public void setVnp_TransactionNo(String vnp_TransactionNo) {
        this.vnp_TransactionNo = vnp_TransactionNo;
    }

    public String getVnp_ResponseCode() {
        return vnp_ResponseCode;
    }

    public void setVnp_ResponseCode(String vnp_ResponseCode) {
        this.vnp_ResponseCode = vnp_ResponseCode;
    }

    public String getVnp_TransactionStatus() {
        return vnp_TransactionStatus;
    }

    public void setVnp_TransactionStatus(String vnp_TransactionStatus) {
        this.vnp_TransactionStatus = vnp_TransactionStatus;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }

    public String getVnp_SecureHashType() {
        return vnp_SecureHashType;
    }

    public void setVnp_SecureHashType(String vnp_SecureHashType) {
        this.vnp_SecureHashType = vnp_SecureHashType;
    }

    public String getVnp_SecureHash() {
        return vnp_SecureHash;
    }

    public void setVnp_SecureHash(String vnp_SecureHash) {
        this.vnp_SecureHash = vnp_SecureHash;
    }
}
