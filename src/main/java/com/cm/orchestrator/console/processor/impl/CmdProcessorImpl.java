package com.cm.orchestrator.console.processor.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.cm.orchestrator.console.processor.CmdProcessor;
import com.cm.orchestrator.factory.TaskFactory;
import com.cm.orchestrator.model.Task;
import com.cm.orchestrator.service.TaskService;
import com.cm.orchestrator.service.impl.TaskServiceImpl;

public class CmdProcessorImpl implements CmdProcessor {

    private TaskService taskService;
    private TaskFactory taskFactory;

    public CmdProcessorImpl(ScheduledThreadPoolExecutor executor) {
        this.taskService = new TaskServiceImpl(executor);
        this.taskFactory = new TaskFactory(executor);
    }

    public void process(String cmd, ScheduledThreadPoolExecutor executor) {

        String[] tokens = cmd.substring(5, cmd.length()).split(" ");
        String commend = tokens[0];

        if (commend.equalsIgnoreCase("--list")) {
            boolean showFullDetails = (tokens.length >= 2 && tokens[1].equalsIgnoreCase("full")) ? true : false;
            taskService.listTasks(showFullDetails);
            return;
        } else if (commend.equalsIgnoreCase("--history")) {
            boolean clearHistory = (tokens.length >= 2 && tokens[1].equalsIgnoreCase("clear")) ? true : false;
            if(clearHistory) {
                taskService.clearHistory();
            } else {
                taskService.printHistory();
            }
            return;
        }

        String taskName = tokens[1];
        if (commend.equalsIgnoreCase("--add")) {
            Task newTask = taskFactory.getTask(taskName, tokens[2], Integer.valueOf(tokens[3]),
                    taskService.getHistoryList());
            if (newTask == null) {
                System.out.println("invalid task-type:" + tokens[2]);
                return;
            }
            taskService.addTask(taskName, newTask);
        } else if (commend.equalsIgnoreCase("--delete")) {
            taskService.deleteTask(taskName);
        } else if (commend.equalsIgnoreCase("--add-depend")) {
            if (tokens.length < 3) {
                System.out.println("invalid parameters");
                return;
            }
            taskService.addDependency(taskName, tokens[2]);
        } else if (commend.equalsIgnoreCase("--remove-depend")) {
            if (tokens.length < 3) {
                System.out.println("invalid parameters");
                return;
            }
            taskService.removeDependency(taskName, tokens[2]);
        } else if (commend.equalsIgnoreCase("--info")) {
            taskService.prinTaskInfo(taskName);
        } else if (commend.equalsIgnoreCase("--run")) {
            if (tokens.length <= 2) {
                taskService.runTask(taskName);
            } else {
                taskService.scheduleTask(taskName, Integer.valueOf(tokens[2]));
            }
        } else if (commend.equalsIgnoreCase("--stop")) {
            taskService.stopTask(taskName);
        } else {
            System.out.println("invalid cmd: " + cmd);
        }
    }
}
