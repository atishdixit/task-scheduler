package com.task.sche.utils;

import com.task.sche.model.JobsDetails;
import com.task.sche.model.ScheduleType;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@UtilityClass
public class Utils {
    private static Map<ScheduleType, Calculate<JobsDetails>> ops = new HashMap<>();
     static {
         ops.put(ScheduleType.EXACT_TIME, (jobDetails)->{
             LocalDateTime currentTime = now();
             return Duration.between(jobDetails.getExactTime(), currentTime).getSeconds();
         });

         ops.put(ScheduleType.MINUTES, (jobDetails)->{
             return Duration.between(jobDetails.getCreatedAt(), now()).getSeconds() - (jobDetails.getTime() * 60);
         });

         ops.put(ScheduleType.SECONDS, (jobDetails)->{
             return Duration.between(jobDetails.getCreatedAt(), now()).getSeconds() -  jobDetails.getTime();
         });
     }

    public static long calculateExecutionTime(final JobsDetails jobsDetails ) {
        return ops.get(jobsDetails.getScheduleType()).execute(jobsDetails);
    }
}
