package com.n3.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {
//    @Scheduled(fixedRate = 10000)
//    public void scheduleFixedRateTask() {
//        System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000);
//    }
}
