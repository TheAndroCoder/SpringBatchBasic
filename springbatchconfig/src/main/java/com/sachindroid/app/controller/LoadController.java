package com.sachindroid.app.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/load")
public class LoadController {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;
    @GetMapping
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Map<String, JobParameter> map=new HashMap<>();
        map.put("time",new JobParameter(System.currentTimeMillis()));
        JobExecution execution=jobLauncher.run(job,new JobParameters(map));
        while(execution.isRunning()){
            System.out.println("Batch is Running");
        }
        return execution.getStatus();
    }
}
