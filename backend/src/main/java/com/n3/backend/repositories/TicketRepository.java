package com.n3.backend.repositories;

import com.n3.backend.entities.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    TicketEntity findByCarId(int carId);
    Page<TicketEntity> searchByCarUserFullnameContainsAndAndCarCodeContains(String userFullname, String carCode, org.springframework.data.domain.Pageable pageable);
}
