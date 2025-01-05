package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GateService {
    public String openGate(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://darling-shark-unique.ngrok-free.app/api/open_door";
        String result = restTemplate.getForEntity(url, String.class).getBody();
        return result;
    }
}
