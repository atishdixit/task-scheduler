package com.task.sche.tasks.factory;

import com.task.sche.tasks.stratigy.CleanUpDataTask;
import com.task.sche.tasks.stratigy.Task;
import com.task.sche.tasks.stratigy.MigrateDataTask;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {

    public Task getTask(TaskName taskName){
        Task task = null;
    switch (taskName){
        case MIGRATE_DATA:
            task = new MigrateDataTask();
            break;
        case CLEAN_UP:
            task = new CleanUpDataTask();
            break;
    }
        return task;
    }


}
