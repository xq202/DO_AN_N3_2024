package com.n3.backend.repositories;

import com.n3.backend.entities.PackingInformation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackingInformationRepository extends JpaRepository<PackingInformation, Integer> {
    default PackingInformation findFirst(){
        Pageable pageable = PageRequest.of(0, 1);
        return findAll(pageable).stream().findFirst().orElse(null);
    }
}
