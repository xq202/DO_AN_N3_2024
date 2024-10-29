package com.n3.backend.repositories;

import com.n3.backend.entities.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    TicketEntity findByCarId(int carId);
//    Page<TicketEntity> searchByCarUserFullnameContainsAndAndCarCodeContains(String userFullname, String carCode, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT t FROM TicketEntity t WHERE t.car.code like ?1 AND t.car.user.fullname like ?2 AND (t.ticketType.id = ?3 OR ?3 = 0) AND (t.startDate >= ?4) AND (t.endDate <= ?5)")
    Page<TicketEntity> search(String carCode, String userFullname, int ticketTypeId, Date startDate, Date endDate, org.springframework.data.domain.Pageable pageable);
}
