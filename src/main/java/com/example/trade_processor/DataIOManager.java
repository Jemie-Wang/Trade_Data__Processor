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


public class DataIOManager {

    private final int CHUNK_SIZE; // set chunk size to process
    private static final Logger logger = Logger.getLogger("com.wombat.nose");
    private final String inputPath;
    private final String outputCsvPath;
    private final GraphPanel mainPanel;
    private final String symbolInterested;

    public DataIOManager(String inputPath, String outputCsvPath, String outputGraphPath, String symbol, int chunk_size){
        this.inputPath = inputPath;
        this.outputCsvPath = outputCsvPath;
        this.symbolInterested = symbol;
        this.CHUNK_SIZE = chunk_size;
        this.mainPanel = new GraphPanel(outputGraphPath);
    }

    public void execute() {
        logger.log(Level.INFO, "Processing data for date " + java.time.LocalDate.now());
        Path path = Paths.get(inputPath);

        // Calculate the number of chunks
        long lineCount = 0;
        try {
            lineCount = Files.lines(path).count();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Trouble reading input data", e);
        }
        int chunkCount = (int) Math.ceil((double) lineCount / CHUNK_SIZE);
        DataTypeSorter sorter = new DataTypeSorter(this.mainPanel, this.symbolInterested);

        // Read and process data by chunk
        for (int i = 0; i < chunkCount; i++) {
            int skip = i * CHUNK_SIZE;
            int limit = Math.min(CHUNK_SIZE, (int) (lineCount - skip));
            try (Stream<String> lines = Files.lines(path).skip(skip).limit(limit)) {
                lines.forEachOrdered(sorter::updateRecords);
            }
            catch (IOException e){
                logger.log(Level.WARNING, "Trouble reading input data", e);
            }
        }

        // Get the processed data and save in csv
        String[] res = sorter.getRecords();
        path = Paths.get(outputCsvPath);
        String csv = Arrays.stream(res)
                .map(row -> String.join(",", row.split(",")))
                .collect(Collectors.joining("\n"));

        try {
            logger.log(Level.INFO,"Writing output to csv");
            Files.write(path, csv.getBytes());
        } catch (IOException e) {
            logger.log(Level.WARNING,"Trouble writing data to " + path + ": " + e.getMessage(), e);
        }

        // Generate the graph
        try {
            logger.log(Level.INFO,"Generating the graph");
            mainPanel.createAndShowGui(mainPanel);
        } catch (IOException e) {
            logger.log(Level.WARNING,"Trouble generating the graph", e);
        }
    }

    public static void main(String[] agrs){
        final String inputPath = "src/main/resources/trades.csv";
        final String outputCsvPath = "src/main/resources/output.csv";
        final String outputGraphPath = "src/main/resources/AAA.png";
        DataIOManager p = new DataIOManager(inputPath, outputCsvPath, outputGraphPath, "AAA", 1000);
        p.execute();
    }

}
