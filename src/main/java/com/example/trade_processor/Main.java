package com.example.trade_processor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static int count = 0;
    private static Logger logger = Logger.getLogger("com.wombat.nose");

    public static void main(String[] args) {
        logger.log(Level.INFO, "System start at UTC time " + java.time.Clock.systemUTC().instant());
        MyTaskExecutor task = new MyTaskExecutor(logger);
        task.startExecutionAt(15, 02, 50);

//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            logger.log(Level.INFO, "System stopped at UTC time " + java.time.Clock.systemUTC().instant());
//        }));
    }
}

class MyTaskExecutor {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    Processor myTask;
    private static Logger logger;

    public MyTaskExecutor(Logger logger) {
        myTask = new Processor();
        this.logger = logger;
    }

    public void startExecutionAt(int targetHour, int targetMin, int targetSec) {
        Runnable taskWrapper = new Runnable() {
            @Override
            public void run() {
                myTask.execute();
                // Schedule the next task
                startExecutionAt(targetHour, targetMin, targetSec);
            }
        };

        // Submit the current task with a time delay
        long delay = computeNextDelay(targetHour, targetMin, targetSec);
        executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);
    }

    private long computeNextDelay(int targetHour, int targetMin, int targetSec) {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.systemDefault();

        // Calculate the time for the next task
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
//        ZonedDateTime zonedNextTarget = ZonedDateTime.of(localNow, ZoneId.of("America/New_York")).withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
        ZonedDateTime zonedNextTarget = ZonedDateTime.of(localNow, ZoneId.of("America/New_York")).withSecond(targetSec);
//        if (zonedNow.compareTo(zonedNextTarget) > 0) zonedNextTarget = zonedNextTarget.plusDays(1);
        if (zonedNow.compareTo(zonedNextTarget) > 0) zonedNextTarget = zonedNextTarget.plusMinutes(1);

        // Calculate the time difference to the next task
        Duration duration = Duration.between(zonedNow, zonedNextTarget);
        logger.log(Level.INFO, "Next task schedule at " + zonedNextTarget);
        return duration.getSeconds();
    }

    public void stop() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyTaskExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
