package com.task.sche.tasks.factory;

import com.task.sche.tasks.stratigy.CleanUpDataTask;
import com.task.sche.tasks.stratigy.Task;
import com.task.sche.tasks.stratigy.MigrateDataTask;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {

    public Task getTask(TaskName taskName){
        return switch (taskName){
            case MIGRATE_DATA-> new MigrateDataTask();
            case CLEAN_UP-> new CleanUpDataTask();
            default -> throw new IllegalArgumentException("Unknown type");
        };
    }
}
