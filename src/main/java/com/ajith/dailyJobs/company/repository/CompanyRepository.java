package com.ajith.dailyJobs.company.repository;

import com.ajith.dailyJobs.company.entity.Company;
import com.ajith.dailyJobs.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository< Company ,Integer > {
    Optional <Company> findByCompanyEmail (String email);

    Optional< Company> findByEmailToken (String token);

    boolean existsByCompanyEmail (String companyEmail);

    boolean existsByCompanyName (String companyName);

    Optional< Company> findByCompanyId (Integer companyId);

    Optional< Company> findByWorkerId (Long id);
}
