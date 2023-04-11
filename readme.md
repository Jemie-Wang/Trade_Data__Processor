- How to verify & test the solution.<br>
    To validate the correctness of the data processing, I performed unit tests that covered scenarios where a symbol had multiple or single trades. I wrote test cases to generate sample input data for each scenario, and then executed the solution against the generated data.

    To verify that the program could read and write files successfully, I conducted multiple tests and checked the logs and output files.

    To verify the correctness of the generated images, I used Excel to create corresponding charts and compared them to the images generated by the program.

    To verify that the program could run at a specified time, I reduced the program's execution cycle to every minute and checked the log information to determine if it was capable of running at a fixed time every day.
- Thoughts on how you would set up the task to run on a daily basis.<br>
    I researched and found that many people prefer to use ScheduledExecutorService to achieve scheduled execution. The simplest approach is to use the method `scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    ` to make the program run periodically at fixed time intervals.

    However, it is worth noting that during daylight saving time in the United States, the program's running time interval should not be 24 hours between the transition from winter to summer time and vice versa.

    Therefore, I opted to use `schedule(Runnable command, long delay, TimeUnit unit)` and calculate the time interval until the next scheduled execution every time the program runs. This approach ensures that the execution time does not drift and eliminates the need to consider the transition between winter and summer time.

- Thoughts on how you would monitor the ongoing execution of your task.<br>
    I am using the logging to monitor the status of exection with records of info, error, and warning, I think we could also adopt the alert to notify the team about any fatal through email/ SMS.

- Thoughts on why you chose your OS/Language/tools.<br>
    I used Java because this is the languages I have most experience in, to finish a quick task I would perfer that. I used java.awt for drawing because it is a built-in Java library. It is simple and lightweight solution for basic drawing needs, considering we are only required to draw one simple line graph.