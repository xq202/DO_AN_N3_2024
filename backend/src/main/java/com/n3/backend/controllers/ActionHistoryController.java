package com.n3.backend.controllers;

import com.n3.backend.dto.ActionHistory.ActionHistory;
import com.n3.backend.dto.ActionHistory.ActionHistoryRequest;
import com.n3.backend.dto.ActionHistory.ActionHistorySearchRequest;
import com.n3.backend.dto.ApiResponse;
import com.n3.backend.services.ActionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/action-history")
public class ActionHistoryController {
    @Autowired
    ActionHistoryService actionHistoryService;

    @GetMapping("")
    public ApiResponse<List<ActionHistory>> search(@ModelAttribute ActionHistorySearchRequest request){
        return actionHistoryService.getAll(request);
    }

    @GetMapping("/test")
    public ApiResponse test(){
        return new ApiResponse(true, 200, null, "success");
    }

    @PostMapping("")
    public ApiResponse<ActionHistory> add(@RequestBody ActionHistoryRequest request){
        return actionHistoryService.addActionHistory(request);
    }
}
