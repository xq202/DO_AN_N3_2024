package com.n3.backend;

import com.n3.backend.entities.PackingInfomation;
import com.n3.backend.repositories.PackingInfomationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @GetMapping("/")
    public String hello(){
        return "hello";
    }

    @Bean
    public CommandLineRunner createPacingInfo(PackingInfomationRepository repository){
        return (args) -> {
            List<PackingInfomation> list = repository.findAll();

            if(list.size() == 0){
                PackingInfomation packingInfomation = new PackingInfomation();
                packingInfomation.setName("Packing");
                packingInfomation.setTotalSlotAvailable(100);
                packingInfomation.setTotalSlot(100);
                packingInfomation.setMaxSlotBooked(10);
                packingInfomation.setTotalSlotBooked(10);

                repository.save(packingInfomation);
            }
        };
    }
}
