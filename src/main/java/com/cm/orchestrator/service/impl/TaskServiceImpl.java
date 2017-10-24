package com.cm.orchestrator.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cm.orchestrator.model.Task;
import com.cm.orchestrator.model.TaskStatus;
import com.cm.orchestrator.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private ConcurrentMap<String, Task> tasksMap;
    private List<TaskStatus> historyList;
    private ScheduledThreadPoolExecutor executor;

    public TaskServiceImpl(ScheduledThreadPoolExecutor executor) {
        this.tasksMap = new ConcurrentHashMap<>();
        this.historyList = new LinkedList<>();
        this.executor = executor;
    }

    @Override
    public List<TaskStatus> getHistoryList() {
        return this.historyList;
    }    
    
    @Override
    public void addTask(String name, Task task) {
        if (tasksMap.containsKey(name)) {
            System.out.println("task [" + name + "] already created");
            return;
        }
        tasksMap.put(name, task);
    }

    @Override
    public void deleteTask(String name) {
        if (!tasksMap.containsKey(name)) {
            System.out.println("task [" + name + "] does not exit");
            return;
        }
        tasksMap.remove(name);
    }

    @Override
    public void addDependency(String name, String dependency) {
        if (!tasksMap.containsKey(name)) {
            System.out.println("task: [" + name + "] does not exist");
            return;
        } else if (!tasksMap.containsKey(dependency)) {
            System.out.println("task: [" + dependency + "] does not exist");
            return;
        }
        tasksMap.get(name).getDependencies().add(tasksMap.get(dependency));
    }

    @Override
    public void removeDependency(String name, String dependency) {
        if (!tasksMap.containsKey(name)) {
            System.out.println("task: [" + name + "] does not exist");
            return;
        } else if (!tasksMap.containsKey(dependency)) {
            System.out.println("task: [" + dependency + "] does not exist");
            return;
        }
        tasksMap.get(name).getDependencies().remove(tasksMap.get(dependency));
    }

    @Override
    public void listTasks(boolean showFullDetails) {
        for (String name : tasksMap.keySet()) {
            if(showFullDetails) {
                System.out.println(tasksMap.get(name).toString());
            } else {
                System.out.println(name);
            }
        }
    }

    @Override
    public void prinTaskInfo(String name) {
        if (!tasksMap.containsKey(name)) {
            System.out.println("task [" + name + "] does not exit");
            return;
        }
        System.out.println(tasksMap.get(name).toString());
    }

    @Override
    public void runTask(String name) {
        if (!tasksMap.containsKey(name)) {
            System.out.println("task [" + name + "] does not exit");
            return;
        }
        executor.execute(tasksMap.get(name));
    }

    @Override
    public void scheduleTask(String name, int seconds) {
        if (!tasksMap.containsKey(name)) {
            System.out.println("task [" + name + "] does not exit");
            return;
        }
        Task task = tasksMap.get(name);
        task.setStatus(Task.Status.RUNNING);
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, 0, seconds, TimeUnit.SECONDS);
        task.setFuture(future);
    }

    @Override
    public void stopTask(String name) {
        if (!tasksMap.containsKey(name)) {
            System.out.println("task [" + name + "] does not exit");
            return;
        }
        Task task = tasksMap.get(name);
        task.setStatus(Task.Status.STOPPED);
        task.getFuture().cancel(false);
        executor.remove(task);
    }
    
    @Override
    public void printHistory() {
        System.out.println("Task Run History:");
        for(TaskStatus ts : historyList) {
            System.out.println(ts.toString());
        }
        System.out.println("Num of completed tasks:" + executor.getCompletedTaskCount());
    }

    @Override
    public void clearHistory() {
        historyList.clear();
    }
}
