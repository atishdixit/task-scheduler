package com.task.sche.repository;

import com.task.sche.model.JobsDetails;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobsDetails, UUID> {

  List<JobsDetails> findAllByExecuted(boolean executed);

}
