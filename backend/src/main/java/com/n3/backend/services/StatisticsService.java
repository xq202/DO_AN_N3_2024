package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Statistics.Income;
import com.n3.backend.dto.Statistics.IncomeDto;
import com.n3.backend.dto.Statistics.UserSpending;
import com.n3.backend.dto.TicketType.TicketType;
import com.n3.backend.entities.TicketTypeEntity;
import com.n3.backend.repositories.InvoiceRepository;
import com.n3.backend.repositories.TicketRepository;
import com.n3.backend.repositories.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TicketTypeRepository ticketTypeRepository;
    public ApiResponse spendingReport(){
        List<UserSpending> list = invoiceRepository.reportTotalIncome(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "total"))).getContent();

        return new ApiResponse(true, 200, list, "success");
    }

    public ApiResponse incomeReport(){
        Timestamp endDate = new Timestamp(System.currentTimeMillis());
        Timestamp startDate = Timestamp.valueOf(endDate.toLocalDateTime().minusMonths(6));

        List<TicketTypeEntity> ticketTypes = ticketTypeRepository.findAll();

        List<IncomeDto> result = new ArrayList<>();

        for (TicketTypeEntity ticketType : ticketTypes) {
            List<Income> list = ticketRepository.reportTotalIncome(startDate, endDate, ticketType.getId(), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "month"))).getContent();

            int total = 0;
            double totalIncome = 0;

            for (Income income : list) {
                total += income.getTotal();
                totalIncome += income.getTotalIncome();
            }

            result.add(new IncomeDto(ticketType.getName(), total, totalIncome, list));
        }

        return new ApiResponse(true, 200, result, "success");
    }
}
