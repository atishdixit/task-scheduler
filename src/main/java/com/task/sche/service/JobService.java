package com.task.sche.service;

import com.task.sche.model.JobsDetails;
import com.task.sche.model.ScheduleType;
import com.task.sche.repository.JobRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.task.sche.tasks.factory.TaskFactory;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.task.sche.constant.AppConstants.TASK_PROCESSED_LOG;

@AllArgsConstructor
@Service
@Slf4j
public class JobService {

  private final ScheduledExecutorService executorService;
  private final JobRepository jobRepository;
  private final TaskFactory taskFactory;

  @PostConstruct
  public void init() {
    log.info("rescheduling non-executed tasks...");
    List<JobsDetails> tasks = jobRepository.findAllByExecuted(false);
    log.info("{} non-executed tasks found.", tasks.size());
    tasks.forEach(this::scheduleTask);
  }

  @Async
  public CompletableFuture<Void> executeTask(UUID taskId) {
    return CompletableFuture.runAsync(
        () -> {
          JobsDetails JobsDetails = jobRepository.findById(taskId).orElse(null);
          if (JobsDetails != null && !JobsDetails.isExecuted()) {

            taskFactory.getTask(JobsDetails.getTask()).execute();

            log.info(TASK_PROCESSED_LOG, JobsDetails.getCreatedAt(), JobsDetails.getDescription());
            calculateExecutionTimeAndLatency(JobsDetails);
            JobsDetails.setExecuted(true);
            jobRepository.save(JobsDetails);
          }
        });
  }



  private static void calculateExecutionTimeAndLatency(JobsDetails JobsDetails) {
    JobsDetails.setExecutedAt(LocalDateTime.now());
    LocalDateTime currentTime = LocalDateTime.now();
    long latencyInSeconds;

    if (ScheduleType.EXACT_TIME.equals(JobsDetails.getScheduleType())) {
      Duration diff = Duration.between(JobsDetails.getExactTime(), currentTime);
      latencyInSeconds = diff.getSeconds();
    } else {
      Duration diff = Duration.between(JobsDetails.getCreatedAt(), currentTime);
      long elapsedSeconds = diff.getSeconds();

      if (ScheduleType.MINUTES.equals(JobsDetails.getScheduleType())) {
        latencyInSeconds = elapsedSeconds - (JobsDetails.getTime() * 60);
      } else {
        latencyInSeconds = elapsedSeconds - JobsDetails.getTime();
      }
    }

    JobsDetails.setLatency(latencyInSeconds);
  }

  public JobsDetails scheduleTask(JobsDetails JobsDetails) {
    JobsDetails savedTask = jobRepository.save(JobsDetails);
    long delay = calculateDelay(JobsDetails);
    executorService.schedule(() -> handleScheduledTask(JobsDetails), delay, TimeUnit.SECONDS);
    return savedTask;
  }

  private long calculateDelay(JobsDetails JobsDetails) {
    long delay;
    LocalDateTime executionTime;

    if (ScheduleType.EXACT_TIME.equals(JobsDetails.getScheduleType())) {
      executionTime = JobsDetails.getExactTime();
    } else if (ScheduleType.MINUTES.equals(JobsDetails.getScheduleType())) {
      executionTime = JobsDetails.getCreatedAt().plusMinutes(JobsDetails.getTime());
    } else {
      executionTime = JobsDetails.getCreatedAt().plusSeconds(JobsDetails.getTime());
    }

    Duration duration = Duration.between(LocalDateTime.now(), executionTime);
    delay = duration.isNegative() || duration.isZero() ? 0 : duration.getSeconds();

    return Math.max(delay, 0);
  }

  private void handleScheduledTask(JobsDetails JobsDetails) {
    try {
      JobsDetails latestJobsDetails = jobRepository.findById(JobsDetails.getId()).orElse(null);
      if (latestJobsDetails != null
          && latestJobsDetails.isUpdated()
          && !latestJobsDetails.isExecuted()) {
        scheduleTask(latestJobsDetails);
      } else {
        executeTask(JobsDetails.getId()).get();
      }
    } catch (Exception e) {
      log.error("something went wrong", e);
    }
  }

  public JobsDetails updateTask(UUID id, JobsDetails updatedJobsDetails) {
    JobsDetails existingJobsDetails = jobRepository.findById(id).orElse(null);
    if (existingJobsDetails == null) {
      return null;
    }
    updateJobsDetails(existingJobsDetails, updatedJobsDetails);
    scheduleTask(existingJobsDetails);
    return existingJobsDetails;
  }

  private void updateJobsDetails(JobsDetails existingJobsDetails, JobsDetails updatedJobsDetails) {
    existingJobsDetails.setDescription(updatedJobsDetails.getDescription());
    existingJobsDetails.setTime(updatedJobsDetails.getTime());
    existingJobsDetails.setScheduleType(updatedJobsDetails.getScheduleType());
    existingJobsDetails.setExactTime(updatedJobsDetails.getExactTime());
    existingJobsDetails.setUpdated(true);
    jobRepository.save(existingJobsDetails);
  }

  public List<JobsDetails> getAllTasks() {
    return jobRepository.findAll();
  }
}
