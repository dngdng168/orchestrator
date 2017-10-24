package com.cm.orchestrator.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskStatus {

    public enum Status {
        SUCCESS, FAIL;
    }

    private String taskName;
    private Date start;
    private Date end;
    private int numOfRetries;
    private Status status;

    public TaskStatus(String taskName, Date start, Date end, int numOfRetries, Status status) {
        super();
        this.taskName = taskName;
        this.start = start;
        this.end = end;
        this.numOfRetries = numOfRetries;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getNumOfRetries() {
        return numOfRetries;
    }

    public void setNumOfRetries(int numOfRetries) {
        this.numOfRetries = numOfRetries;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return "TaskStatus [taskName=" + taskName + ", start=" + formatter.format(start) + ", end="
                + formatter.format(end) + ", numOfRetries=" + numOfRetries + ", status=" + status + "]";
    }

}
