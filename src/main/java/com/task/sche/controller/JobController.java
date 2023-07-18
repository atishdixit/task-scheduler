package com.task.sche.controller;

import com.task.sche.model.JobsDetails;
import com.task.sche.service.JobService;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
@AllArgsConstructor
public class JobController {

  private final JobService jobService;

  @GetMapping
  public ResponseEntity<List<JobsDetails>> getAllTasks() {
    return ResponseEntity.ok(jobService.getAllTasks());
  }

  @PostMapping
  public ResponseEntity<JobsDetails> scheduleTask(@RequestBody JobsDetails jobsDetails) {

    return ResponseEntity.status(HttpStatus.CREATED).body(jobService.scheduleTask(jobsDetails));
  }

  @PutMapping("/{id}")
  public ResponseEntity<JobsDetails> updateTask(
      @PathVariable UUID id, @RequestBody JobsDetails jobsDetails) {
    JobsDetails updatedJobsDetails = jobService.updateTask(id, jobsDetails);
    if (updatedJobsDetails == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(updatedJobsDetails);
  }
}
