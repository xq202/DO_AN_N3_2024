package com.n3.backend.controllers;

import com.n3.backend.dto.ActionHistory.ActionHistory;
import com.n3.backend.dto.ActionHistory.ActionHistoryOut;
import com.n3.backend.dto.ActionHistory.ActionHistoryRequest;
import com.n3.backend.dto.ActionHistory.ActionHistorySearchRequest;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.services.ActionHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/action-history")
public class ActionHistoryController {
    @Autowired
    ActionHistoryService actionHistoryService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ActionHistory>> search(@ModelAttribute ActionHistorySearchRequest request){
        return actionHistoryService.getAll(request);
    }

    @GetMapping("/car/{id}")
    public ApiResponse getActionHistoryByCarId(@PathVariable int id, HttpServletRequest request){
        return actionHistoryService.getByCar(id, request);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ActionHistoryOut> add(@RequestBody ActionHistoryRequest request){
        return actionHistoryService.addActionHistory(request);
    }
}
