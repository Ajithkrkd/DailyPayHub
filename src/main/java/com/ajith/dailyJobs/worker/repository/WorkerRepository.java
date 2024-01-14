package com.ajith.dailyJobs.worker.repository;

import com.ajith.dailyJobs.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface WorkerRepository extends JpaRepository< Worker,Long> {
    Optional < Worker > findByEmail(String email);
    boolean existsByEmail (String email);

    Optional< Worker > findByEmailVerificationToken (String token);
}
