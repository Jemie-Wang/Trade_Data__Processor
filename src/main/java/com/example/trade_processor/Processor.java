package com.example.trade_processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.logging.Logger;


public class Processor{

    private static final int CHUNK_SIZE = 10000; // set chunk size to process
    private static Logger logger = Logger.getLogger("com.wombat.nose");

    public void execute() {
        logger.log(Level.INFO, "Processing data for date " + java.time.LocalDate.now());
        Path path = Paths.get("src/main/resources/trades.csv");

        // Calculate the number of chunks
        long lineCount = 0;
        try {
            lineCount = Files.lines(path).count();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Trouble reading input data", e);
        }
        int chunkCount = (int) Math.ceil((double) lineCount / CHUNK_SIZE);
        RecordsHub rh = new RecordsHub();

        // Read and process data by chunk
        for (int i = 0; i < chunkCount; i++) {
            int skip = i * CHUNK_SIZE;
            int limit = Math.min(CHUNK_SIZE, (int) (lineCount - skip));
            try (Stream<String> lines = Files.lines(path).skip(skip).limit(limit)) {
                lines.forEach(line -> {
                    rh.updateRecords(line);
                });
            }
            catch (IOException e){
                logger.log(Level.WARNING, "Trouble reading input data", e);
            }
        }

        // Get the processed data and save in csv
        String[] res = rh.getRecords();
        path = Paths.get("src/main/resources/output.csv");
        String csv = Arrays.stream(res)
                .map(row -> String.join(",", row.split(",")))
                .collect(Collectors.joining("\n"));

        try {
            Files.write(path, csv.getBytes());
        } catch (IOException e) {
            logger.log(Level.WARNING,"Trouble writing data to " + path + ": " + e.getMessage(), e);
        }

        // Generate the graph
        try {
            rh.getGraph();
        } catch (IOException e) {
            logger.log(Level.WARNING,"Trouble generating the graph", e);
        }
    }

    public static void main(String[] agrs){
        Processor p = new Processor();
        p.execute();
    }

}
