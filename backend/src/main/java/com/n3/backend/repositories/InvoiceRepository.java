package com.n3.backend.repositories;

import com.n3.backend.dto.Statistics.UserSpending;
import com.n3.backend.entities.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
    Page<InvoiceEntity> searchByUserFullnameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCaseAndStatus(String userFullname, String userEmail, String code, int status, org.springframework.data.domain.Pageable pageable);

    @Query("select i.user.fullname as username, i.user.email as email, sum(i.total) as total from InvoiceEntity i where i.status = 1 group by i.user.id")
    Page<UserSpending> reportTotalIncome(Pageable pageable);

    InvoiceEntity findByCode(String code);
}
