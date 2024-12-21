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
import java.time.LocalDateTime;
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

        List<TicketTypeEntity> ticketTypes = ticketTypeRepository.findAll();

        List<IncomeDto> result = new ArrayList<>();

        int months = LocalDate.now().getMonthValue();

        for (TicketTypeEntity ticketType : ticketTypes) {
            int month = months;
            List<Income> list = new ArrayList<>();

            int total = 0;
            double totalIncome = 0;

            int check = 0;
            for (int i = 1; i <= 5; i++) {
                Income income = ticketRepository.reportIncomePerMonth(month, ticketType.getId());
                if (income == null) {
                    int finalMonth = month;
                    int finalCheck = check;
                    income = new Income() {
                        @Override
                        public String getMonth() {
                            return String.valueOf(finalMonth);
                        }

                        @Override
                        public String getYear() {
                            return String.valueOf(LocalDate.now().getYear() - finalCheck);
                        }

                        @Override
                        public int getTotal() {
                            return 0;
                        }

                        @Override
                        public double getTotalIncome() {
                            return 0;
                        }
                    };
                }

                month--;

                if (month == 0) {
                    month = 12;
                    check++;
                }

                totalIncome += income.getTotalIncome();
                total += income.getTotal();

                list.add(income);
            }

            result.add(new IncomeDto(ticketType.getName(), total, totalIncome, list));
        }

        return new ApiResponse(true, 200, result, "success");
    }
}
