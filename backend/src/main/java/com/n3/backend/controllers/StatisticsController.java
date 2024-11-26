package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.Statistics.Income;
import com.n3.backend.dto.Statistics.IncomeDto;
import com.n3.backend.dto.Statistics.UserSpending;
import com.n3.backend.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;
    @GetMapping("/spending")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserSpending>> spendingReport(){
        return statisticsService.spendingReport();
    }

    @GetMapping("/income")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<IncomeDto>> incomeReport(){
        return statisticsService.incomeReport();
    }

}
