package com.task.sche.tasks.stratigy;

import com.task.sche.tasks.factory.TaskName;

import java.util.Date;

public class MigrateDataTask implements Task {

    private TaskName taskName = TaskName.MIGRATE_DATA;

    @Override
    public void execute() {
        System.out.println(taskName.name()+ " Job Started AT "+new Date());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(taskName.name()+ " Job Completed AT "+new Date());
    }
}
