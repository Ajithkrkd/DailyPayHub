package com.ajith.dailyJobs.admin.worker;

import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/admin")
@RequiredArgsConstructor
public class UserManagement {

    private  final WorkerService workerService;


    @PostMapping ("/blockWorker/{workerId}")
    public ResponseEntity < BasicResponse > makeBlockUser(@PathVariable Long workerId)
    {
        return  workerService.blockUser ( workerId );
    }
}
