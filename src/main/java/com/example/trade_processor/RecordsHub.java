package com.example.trade_processor;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class RecordsHub {
    Map<String, Record> map;
    GraphPanel mainPanel = new GraphPanel();
    private Logger logger = Logger.getLogger("com.wombat.nose");
    RecordsHub() {
        map = new HashMap<>();
    }

    void updateRecords(String trade) {
        String[] parameter = trade.split(",");
        int timeStamp = Integer.parseInt(parameter[0]);
        String symbol = parameter[1];
        int volume = Integer.parseInt(parameter[2]);
        int price = Integer.parseInt(parameter[3]);
        // Check if this is a valid record
        if(symbol.length() < 3 || volume <= 0){
            logger.log(Level.WARNING, "Invalid record format: " + trade);
        }
        // Create record or update the record
        if (map.containsKey(symbol)) {
            map.get(symbol).update(timeStamp, volume, price);
        } else {
            map.put(symbol, new Record(timeStamp, symbol, volume, price));
        }
        // If belongs to AAA, collect the data as an point on the graph
        if (symbol.equals("AAA")) this.mainPanel.addPoint(timeStamp, price);
    }

    // Return all the records
    String[] getRecords() {
        String[] records = map.values()
                .stream()
                .map(Record::toString)
                .toArray(String[]::new);
        return records;
    }

    // Generate the graph for AAA
    void getGraph() throws IOException {
        this.mainPanel.createAndShowGui(mainPanel);
    }
}
