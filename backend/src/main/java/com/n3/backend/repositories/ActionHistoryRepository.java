package com.n3.backend.repositories;

import com.n3.backend.entities.ActionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ActionHistoryRepository extends JpaRepository<ActionHistoryEntity, Integer> {
//    @Query("SELECT a, c " +
//            "FROM ActionHistoryEntity a " +
//            "inner join CarEntity c on c.id = a.car.id " +
//            "WHERE c.code = ?1 and a.action = ?2")
    Page<ActionHistoryEntity> searchByCarCodeContainingIgnoreCaseAndActionContainingIgnoreCaseAndCreatedAtBetween(String code, String action, Timestamp startDate, Timestamp endDate, Pageable pageable);

    Page<ActionHistoryEntity> searchAllByCarIdAndActionContainingIgnoreCaseAndCreatedAtBetween(int id, String action, Timestamp startDate, Timestamp endDate, Pageable pageable);

    ActionHistoryEntity findTopByActionAndCarIdOrderByCreatedAtDesc(String action, int carId);
}
