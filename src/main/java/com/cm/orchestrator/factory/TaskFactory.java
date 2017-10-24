package com.cm.orchestrator.factory;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.cm.orchestrator.model.BadTask;
import com.cm.orchestrator.model.SimpleTask;
import com.cm.orchestrator.model.Task;
import com.cm.orchestrator.model.TaskStatus;

public class TaskFactory {

    private ScheduledThreadPoolExecutor executor;

    public TaskFactory(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public Task getTask(String taskName, String taskType, int numOfRetries, List<TaskStatus> historyList) {

        if (taskName == null || taskType == null || taskType.isEmpty()) {
            return null;
        }

        if (taskType.equalsIgnoreCase("simple")) {
            return new SimpleTask(taskName, numOfRetries, historyList, executor);
        } else if (taskType.equalsIgnoreCase("bad")) {
            return new BadTask(taskName, numOfRetries, historyList, executor);
        }

        return null;
    }
}
