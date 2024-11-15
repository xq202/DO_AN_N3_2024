package com.n3.backend.repositories;

import com.n3.backend.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    public UserEntity getById(int id);

    public UserEntity getByEmail(String email);

    Page<UserEntity> findByFullnameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndIsAdmin(String fullname, String email, boolean isAdmin, org.springframework.data.domain.Pageable pageable);

}
