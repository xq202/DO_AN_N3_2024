package com.n3.backend.repositories;

import com.n3.backend.entities.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketTypeEntity, Integer> {
    public
    List<TicketTypeEntity> findAll();

    TicketTypeEntity getOne(int id);

    TicketTypeEntity findFirstByType(String type);
}
