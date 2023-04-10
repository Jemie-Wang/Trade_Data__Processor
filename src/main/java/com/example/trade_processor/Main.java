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
    private static final Logger logger = Logger.getLogger("com.wombat.nose");
    /**
     * To modify the input output path, the symbol to draw graph with, the lines each time read from the csv,
     * or the time to run the task,
     * please modify the variable below.
     * **/
    private static final String inputPath = "src/main/resources/trades.csv";
    private static final String outputCsvPath = "src/main/resources/output.csv";
    private static final String outputGraphPath = "src/main/resources/AAA.png";
    private static final String symbolInterested = "AAA";
    private static final int CHUNK_SIZE = 10000;
    private static final int targetHour = 16;
    private static final int targetMin = 0;
    private static final int targetSec = 0;

    public static void main(String[] args) {
        logger.log(Level.INFO, "System start at UTC time " + java.time.Clock.systemUTC().instant());
        MyTaskExecutor task = new MyTaskExecutor();
        task.startExecutionAt(targetHour, targetMin, targetSec);
    }

    static class MyTaskExecutor {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        DataIOManager myTask;


        public MyTaskExecutor() {
            myTask = new DataIOManager(inputPath, outputCsvPath, outputGraphPath, symbolInterested, CHUNK_SIZE);
        }

        public void startExecutionAt(int targetHour, int targetMin, int targetSec) {
            Runnable taskWrapper = () -> {
                myTask.execute();
                // Schedule the next task
                startExecutionAt(targetHour, targetMin, targetSec);
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
            // Make the task run on given target time
            ZonedDateTime zonedNextTarget = ZonedDateTime.of(localNow, ZoneId.of("America/New_York")).withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
            if (zonedNow.compareTo(zonedNextTarget) > 0) zonedNextTarget = zonedNextTarget.plusDays(1);
            /**
             * For testing the scheduler, please comment the above two lines, and uncomment the two lines below,
             * This will make the scheduler run on a one minute base at the 1st second of each minute.
             * **/
//            ZonedDateTime zonedNextTarget = ZonedDateTime.of(localNow, ZoneId.of("America/New_York")).withSecond(targetSec);
//            if (zonedNow.compareTo(zonedNextTarget) > 0) zonedNextTarget = zonedNextTarget.plusMinutes(1);

            // Calculate the time difference to the next task
            Duration duration = Duration.between(zonedNow, zonedNextTarget);
            logger.log(Level.INFO, "Next task schedule at " + zonedNextTarget);
            return duration.getSeconds();
        }

//        public void stop() {
//            executorService.shutdown();
//            try {
//                executorService.awaitTermination(1, TimeUnit.DAYS);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MyTaskExecutor.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

}

