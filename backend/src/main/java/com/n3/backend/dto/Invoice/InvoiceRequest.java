package com.n3.backend.dto.Invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvoiceRequest {
    private List<TicketTypeIdCarId> Ids;

    public List<TicketTypeIdCarId> getIds() {
        return Ids;
    }

    public void setIds(List<TicketTypeIdCarId> ids) {
        Ids = ids;
    }
}