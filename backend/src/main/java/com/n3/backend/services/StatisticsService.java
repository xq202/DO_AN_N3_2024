package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Statistics.UserSpending;
import com.n3.backend.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    InvoiceRepository invoiceRepository;
    public ApiResponse spendingReport(){
        List<UserSpending> list = invoiceRepository.reportTotalIncome(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "total"))).getContent();

        return new ApiResponse(true, 200, list, "success");
    }
}
