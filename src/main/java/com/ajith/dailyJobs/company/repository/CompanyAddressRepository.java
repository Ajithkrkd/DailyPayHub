package com.ajith.dailyJobs.company.repository;

import com.ajith.dailyJobs.company.entity.CompanyAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyAddressRepository extends JpaRepository<CompanyAddress,Integer > {
}
