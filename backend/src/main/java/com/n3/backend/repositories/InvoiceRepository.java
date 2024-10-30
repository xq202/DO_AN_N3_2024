package com.n3.backend.repositories;

import com.n3.backend.entities.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
    Page<InvoiceEntity> searchByUserFullnameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCase(String userFullname, String userEmail, String code, org.springframework.data.domain.Pageable pageable);
}
