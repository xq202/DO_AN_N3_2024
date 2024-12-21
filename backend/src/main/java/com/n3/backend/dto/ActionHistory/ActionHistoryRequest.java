package com.n3.backend.dto.ActionHistory;

public class ActionHistoryRequest {
    private String code;
    private String action;

    public ActionHistoryRequest() {
    }

    public ActionHistoryRequest(String code, String action) {
        this.code = code;
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
