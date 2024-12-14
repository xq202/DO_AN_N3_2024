package com.n3.backend.repositories;

import com.n3.backend.entities.CarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Integer> {
    List<CarEntity> findByNameContaining(String name);

    Page<CarEntity> findByCodeContaining(String code, Pageable pageable);
    CarEntity findByCodeAndIsDeleted(String code, boolean isDeleted);

    Page<CarEntity> searchByUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCaseAndUserIdAndIsDeleted(String name, String code, int id, boolean isDeleted, Pageable pageable);
    Page<CarEntity> searchByUserEmailContainingIgnoreCaseAndCodeContainingIgnoreCase(String name, String code, Pageable pageable);

    CarEntity findByIdAndIsDeleted(int id, boolean isDeleted);
}
