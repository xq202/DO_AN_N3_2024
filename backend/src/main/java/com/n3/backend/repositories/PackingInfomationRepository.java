package com.n3.backend.repositories;

import com.n3.backend.entities.PackingInfomation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackingInfomationRepository extends JpaRepository<PackingInfomation, Integer> {
    default PackingInfomation findFirst(){
        Pageable pageable = PageRequest.of(0, 1);
        return findAll(pageable).stream().findFirst().orElse(null);
    }
}
