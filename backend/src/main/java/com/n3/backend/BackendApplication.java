package com.n3.backend;

import com.n3.backend.entities.PackingInformation;
import com.n3.backend.repositories.PackingInformationRepository;
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
    public CommandLineRunner createPacingInfo(PackingInformationRepository repository){
        return (args) -> {
            List<PackingInformation> list = repository.findAll();

            if(list.size() == 0){
                PackingInformation packingInformation = new PackingInformation();
                packingInformation.setName("Packing");
                packingInformation.setTotalSlotAvailable(100);
                packingInformation.setTotalSlot(100);
                packingInformation.setMaxSlotBooked(10);
                packingInformation.setTotalSlotBooked(10);
                packingInformation.setTotalSlotBookedAvailable(10);
                packingInformation.setPricePerHour(10000);

                repository.save(packingInformation);
            }
        };
    }
}
