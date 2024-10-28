package com.n3.backend.repositories;

import com.n3.backend.entities.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
    Page<InvoiceEntity> searchByUserFullnameContainsAndUserEmailContainsAndIdContains(String userFullname, String userEmail, int id, org.springframework.data.domain.Pageable pageable);
}
