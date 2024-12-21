package com.n3.backend;

import com.n3.backend.entities.PackingInformation;
import com.n3.backend.entities.TicketTypeEntity;
import com.n3.backend.repositories.PackingInformationRepository;
import com.n3.backend.repositories.TicketTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
@RestController
public class BackendApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
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
                packingInformation.setTotalSlotBooked(0);
                packingInformation.setTotalSlotBookedAvailable(0);

                repository.save(packingInformation);
            }
        };
    }

    @Bean
    public CommandLineRunner addTicketType(TicketTypeRepository ticketTypeRepository){
        return (args) -> {
            TicketTypeEntity ticketTypeEntity = ticketTypeRepository.findFirstByType("hour");

            if(ticketTypeEntity == null){
                ticketTypeEntity = new TicketTypeEntity();
                ticketTypeEntity.setType("hour");
                ticketTypeEntity.setPrice(10000);
                ticketTypeEntity.setName("Ve luot");

                ticketTypeRepository.save(ticketTypeEntity);
            }

            TicketTypeEntity ticketTypeEntity1 = ticketTypeRepository.findFirstByType("month");
            if(ticketTypeEntity1 == null){
                ticketTypeEntity1 = new TicketTypeEntity();
                ticketTypeEntity1.setType("month");
                ticketTypeEntity1.setPrice(1000000);
                ticketTypeEntity1.setName("Ve thang");
                ticketTypeEntity1.setDuration(30);

                ticketTypeRepository.save(ticketTypeEntity1);
            }
        };
    }

//    @Bean
//    public CommandLineRunner runNgrok(){
//        return (args) -> {
//            try {
//                Runtime.getRuntime().exec("cmd /c start cmd.exe /K \".\\ngrok.exe  tunnel --authtoken 2fZeZ9aVgiPflyy0sxCbsRLKqnd_3xaRzvJKu4gZF8gvqv5Bt --label  edge=edghts_2hVWdGHqI5kTQTJRgyylSJjNr5t http://localhost:8080 \"");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//    }
}
