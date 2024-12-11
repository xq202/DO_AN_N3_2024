package com.n3.backend.dto.VNPay;

public class VnpResponse {
    private String RspCode;
    private String Message;

    public VnpResponse(String rspCode, String message) {
        RspCode = rspCode;
        Message = message;
    }

    public String getRspCode() {
        return RspCode;
    }

    public void setRspCode(String rspCode) {
        RspCode = rspCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
