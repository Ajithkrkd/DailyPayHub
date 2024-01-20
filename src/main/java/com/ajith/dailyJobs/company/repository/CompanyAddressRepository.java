package com.ajith.dailyJobs.company.repository;

import com.ajith.dailyJobs.company.entity.CompanyAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyAddressRepository extends JpaRepository<CompanyAddress,Integer > {
}
