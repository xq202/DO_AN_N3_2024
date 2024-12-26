package com.n3.backend.repositories;

import com.n3.backend.dto.Statistics.Income;
import com.n3.backend.entities.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    Page<TicketEntity> findByInvoiceDetailCarCodeContainingIgnoreCaseAndCreatedAtBetweenAndInvoiceDetailCarUserId(String carCode, Timestamp startDate, Timestamp endDate, int userId, Pageable pageable);
    Page<TicketEntity> findByInvoiceDetailCarCodeContainingIgnoreCaseAndEndDateAfterAndCreatedAtBetweenAndInvoiceDetailCarUserId(String carCode, Timestamp now, Timestamp startDate, Timestamp endDate, int userId, Pageable pageable);

    @Query("SELECT t FROM TicketEntity t WHERE t.invoiceDetail.car.code like concat('%', ?1, '%') AND t.invoiceDetail.car.user.email like concat('%', ?2, '%') AND (t.invoiceDetail.ticketType.id = ?3 OR ?3 = 0) AND t.createdAt between ?4 and ?5 and ((?6 = 1 and t.endDate < now()) or (?6 = 0 and t.endDate >= now()) or ?6 = -1)")
    Page<TicketEntity> search(String carCode, String email, int ticketTypeId, Timestamp startDate, Timestamp endDate, int isExpired, Pageable pageable);

//    Optional<TicketEntity> findAllByCarCodeContainsIgnoreCaseAndCarUserEmailContainsIgnoreCaseAndCreatedAtBetween(String carCode, String email, Timestamp startDate, Timestamp endDate);

    @Query("select month(t.createdAt) as month, year(t.createdAt) as year, count(t.id) as total, sum(t.invoiceDetail.price) as totalIncome from TicketEntity t where t.createdAt between ?1 and ?2 and  t.invoiceDetail.ticketType.id = ?3 group by year(t.createdAt), month(t.createdAt)")
    Page<Income> reportTotalIncome(Timestamp startDate, Timestamp endDate, int ticketTypeId, org.springframework.data.domain.Pageable pageable);

    TicketEntity findFirstByInvoiceDetailCarIdAndEndDateAfter(int carId, Timestamp now);
    TicketEntity findOneByIdAndEndDateAfter(int id, Timestamp now);

    @Query("select month(t.createdAt) as month, year(t.createdAt) as year, count(t.id) as total, sum(t.invoiceDetail.price) as totalIncome from TicketEntity t where month(t.createdAt) = ?1 and t.invoiceDetail.ticketType.id = ?2 group by year(t.createdAt), month(t.createdAt)")
    Income reportIncomePerMonth(int month, int ticketTypeId);
}
