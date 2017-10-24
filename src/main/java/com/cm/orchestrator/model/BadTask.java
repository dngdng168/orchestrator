package com.cm.orchestrator.model;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class BadTask extends Task {

    public BadTask(String name, int numOfRetries, List<TaskStatus> history, ScheduledThreadPoolExecutor executor) {
        super(name, numOfRetries, history, executor);
    }

    @Override
    public void doTask() throws InterruptedException {
        throw new RuntimeException("something went wrong...");
    }
}
