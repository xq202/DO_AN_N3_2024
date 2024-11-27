package com.n3.backend.repositories;

import com.n3.backend.entities.CarEntity;
import com.n3.backend.entities.CurrentPacking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentPackingRepository extends JpaRepository<CurrentPacking, Integer> {
    CurrentPacking findByCarId(int carId);
    boolean existsByCarId(int carId);

    @Query("select c from CurrentPacking c inner join CarEntity e on c.car.id = e.id where e.code like concat('%', ?1, '%') and e.user.email like concat('%', ?2, '%')")
    Page<CurrentPacking> findAllCarPacking(String code, String email, Pageable pageable);
}
