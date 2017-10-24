package com.cm.orchestrator.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class Task implements Runnable {

    public enum Status {
        CREATED, RUNNING, STOPPED, ERROR;
    }

    protected String name;
    protected List<Task> dependencies;
    protected Status status;
    protected int numOfRetries = 0;
    protected List<TaskStatus> history;
    protected ScheduledThreadPoolExecutor executor;
    protected ScheduledFuture<?> future;

    public Task(String name, int numOfRetries, List<TaskStatus> history, ScheduledThreadPoolExecutor executor) {
        super();
        this.name = name;
        this.dependencies = new LinkedList<>();
        this.status = Status.CREATED;
        this.numOfRetries = numOfRetries;
        this.history = history;
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Task> dependencies) {
        this.dependencies = dependencies;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getNumOfRetries() {
        return numOfRetries;
    }

    public void setNumOfRetries(int numOfRetries) {
        this.numOfRetries = numOfRetries;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    @Override
    public String toString() {
        return "Task [name=" + name + ", dependencies=" + dependencies + ", numOfRetries=" + numOfRetries + ", status="
                + status + "]";
    }

    abstract public void doTask() throws Exception;

    @Override
    public void run() {

        Date start = new Date();
        int retry = 0;

        try {
            // execute all dependency tasks
            for (Task t : dependencies) {
                ScheduledFuture<?> r = executor.schedule(t, 0, TimeUnit.SECONDS);
                r.get(); // wait for task to be completed
            }

            // now, perform the task
            boolean done = false;
            while (!done) {
                try {
                    doTask();
                    done = true;
                } catch (Exception e) {
                    retry++;
                    if (retry >= numOfRetries) {
                        throw e;
                    }
                }
            }

            history.add(new TaskStatus(name, start, new Date(), retry, TaskStatus.Status.SUCCESS));
        } catch (Exception e) {
            // task failed
            history.add(new TaskStatus(name, start, new Date(), retry, TaskStatus.Status.FAIL));
            status = Status.ERROR;
            future.cancel(false);
            executor.remove(this);
        }
    }

}
