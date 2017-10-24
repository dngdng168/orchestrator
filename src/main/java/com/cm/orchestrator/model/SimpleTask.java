package com.cm.orchestrator.model;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SimpleTask extends Task {

    public SimpleTask(String name, int numOfRetries, List<TaskStatus> history, ScheduledThreadPoolExecutor executor) {
        super(name, numOfRetries, history, executor);
    }

    @Override
    public void doTask() throws InterruptedException {
        // just idle for 5 seconds
        Thread.sleep(5000);
    }
}
