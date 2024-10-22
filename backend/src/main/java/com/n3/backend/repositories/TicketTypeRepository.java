package com.n3.backend.repositories;

import com.n3.backend.entities.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketTypeEntity, Integer> {
    public ArrayList<TicketTypeEntity> findAll();

    public TicketTypeEntity getOne(int id);
}
