package com.ceiba.library.repository;

import com.ceiba.library.domain.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    Optional<LoanEntity> findByUserIdAndUserType(
            String userId, Integer userType);
}
