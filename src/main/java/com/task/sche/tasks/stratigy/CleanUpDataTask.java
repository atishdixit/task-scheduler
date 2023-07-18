package com.task.sche.tasks.stratigy;

import com.task.sche.tasks.factory.TaskName;

import java.util.Date;

public class CleanUpDataTask implements Task {
    private TaskName taskName = TaskName.CLEAN_UP;

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
