package com.ajith.dailyJobs.company.controller;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.WorkerNotFoundException;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.CompanyRegisterRequest;
import com.ajith.dailyJobs.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/worker/{workerId}/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    @PostMapping("/register")
    public ResponseEntity< BasicResponse > registerCompany(@PathVariable("workerId") String workerId,
            @RequestBody CompanyRegisterRequest companyRegisterRequest
           ) throws WorkerNotFoundException {
        return companyService.registerCompany (companyRegisterRequest , Long.valueOf ( workerId ) );
    }
}
