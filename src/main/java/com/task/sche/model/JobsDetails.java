package com.task.sche.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.task.sche.tasks.factory.TaskName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JobsDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @CreationTimestamp private LocalDateTime createdAt;
  private LocalDateTime exactTime;
  private String description;

  @Enumerated(EnumType.STRING)
  private ScheduleType scheduleType;

  @Enumerated(EnumType.STRING)
  private TaskName task;

  private LocalDateTime executedAt;

  @Column(name = "latency_seconds")
  private long latency;

  private long time;
  private boolean executed;
  private boolean updated;
}
