package com.cm.orchestrator.service;

import java.util.List;

import com.cm.orchestrator.model.Task;
import com.cm.orchestrator.model.TaskStatus;

public interface TaskService {
    public List<TaskStatus> getHistoryList();
    
    public void addTask(String name, Task task);

    public void deleteTask(String name);

    public void addDependency(String name, String dependency);

    public void removeDependency(String name, String dependency);

    public void listTasks(boolean showFullDetails);

    public void prinTaskInfo(String name);

    public void runTask(String name);

    public void scheduleTask(String name, int seconds);

    public void stopTask(String name);
    
    public void printHistory();
    
    public void clearHistory();
}
