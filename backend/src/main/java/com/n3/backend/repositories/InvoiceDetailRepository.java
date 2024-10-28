package com.n3.backend.repositories;

import com.n3.backend.entities.InvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, Integer> {
    List<InvoiceDetailEntity> findAllByInvoiceId(int invoiceId);
}
