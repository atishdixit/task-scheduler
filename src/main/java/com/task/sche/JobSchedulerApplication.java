package com.task.sche;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class JobSchedulerApplication {

  public static void main(String[] args) {
    SpringApplication.run(JobSchedulerApplication.class, args);
  }

}
