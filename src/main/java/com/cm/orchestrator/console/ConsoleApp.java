package com.cm.orchestrator.console;

import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.cm.orchestrator.console.processor.impl.CmdProcessorImpl;

public class ConsoleApp {

    private ScheduledThreadPoolExecutor executor;
    private CmdProcessorImpl taskCmdProcessor;

    public ConsoleApp() {
        executor = new ScheduledThreadPoolExecutor(10);
        executor.setRemoveOnCancelPolicy(true);
        taskCmdProcessor = new CmdProcessorImpl(executor);
    }

    public void start() {
        System.out.println("Welcome to Sample Orchestrator!");
        printHelp();
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.print("=>");
                String cmd = scanner.nextLine();
                if (cmd != null && !cmd.isEmpty() && cmd.length() >= 4) {
                    if (cmd.equals("exit")) {
                        System.out.println("good bye!");
                        break;
                    }
                    if (cmd.equals("help")) {
                        printHelp();
                        continue;
                    }
                    if (cmd.substring(0, 4).equalsIgnoreCase("task")) {
                        taskCmdProcessor.process(cmd, executor);
                        continue;
                    }
                }
                System.out.println("invalid commands: " + cmd);
            }
        } finally {
            scanner.close();
            executor.shutdown();
        }
    }

    public static void printHelp() {
        System.out.println();
        System.out.println("Here are the supported commends:");
        System.out.println("task --add {task-name} {task-type} {numOfRetries}");
        System.out.println("task --delete {task-name}");
        System.out.println("task --add-depend {task-name} {task-name}");
        System.out.println("task --remove-depend {task-name} {task-name}");
        System.out.println("task --list {optional: full}");
        System.out.println("task --info {task-name}");
        System.out.println("task --run {task-name} {seconds}");
        System.out.println("task --stop {task-name}");
        System.out.println("task --history {optional: clear}");
        System.out.println("help");
        System.out.println("exit");
        System.out.println("");
    }

    public static void main(String args[]) {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }
}
