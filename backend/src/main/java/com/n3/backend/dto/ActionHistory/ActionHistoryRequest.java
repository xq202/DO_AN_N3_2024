package com.n3.backend.dto.ActionHistory;

public class ActionHistoryRequest {
    private int id;
    private String code;
    private String action;

    public ActionHistoryRequest() {
    }

    public ActionHistoryRequest(int id, String code, String action) {
        this.id = id;
        this.code = code;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
