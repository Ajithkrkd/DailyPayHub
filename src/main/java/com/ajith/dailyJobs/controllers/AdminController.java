package com.ajith.dailyJobs.controllers;

import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private WorkerService workerService;
    @GetMapping()
    public ResponseEntity<String> sayAdmin(){
        return  ResponseEntity.ok("admin here");
    }

    @PostMapping("/blockUser/{userId}")
    public ResponseEntity< BasicResponse > makeBlockUser(@PathVariable Long userId)
    {
        return  workerService.blockUser ( userId );
    }
}
