package com.ajith.dailyJobs.company.repository;

import com.ajith.dailyJobs.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository< Company ,Integer > {
}
