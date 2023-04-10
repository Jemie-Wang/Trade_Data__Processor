package com.example.trade_processor;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataTypeSorter {
    Map<String, Record> map;
    GraphPanel mainPanel;
    private final Logger logger = Logger.getLogger("com.wombat.nose");
    private final String symbolInterested;

    public DataTypeSorter(GraphPanel mainPanel, String symbolInterested) {
        this.map = new HashMap<>();
        this.mainPanel = mainPanel;
        this.symbolInterested = symbolInterested;
    }

    public void updateRecords(String trade) {
        String[] parameter = trade.split(",");
        int timeStamp = Integer.parseInt(parameter[0]);
        String symbol = parameter[1];
        int volume = Integer.parseInt(parameter[2]);
        int price = Integer.parseInt(parameter[3]);
        // Check if the symbol name or volume is illegal
        if (symbol.length() < 3 || volume <= 0) {
            logger.log(Level.WARNING, "Invalid record format: " + trade);
            return;
        }
        // Create record or update the record
        if (map.containsKey(symbol)) {
            map.get(symbol).update(timeStamp, volume, price);
        } else {
            map.put(symbol, new Record(timeStamp, symbol, volume, price));
        }
        // If belongs to AAA, collect the data as a point on the graph
        if (symbol.equals(symbolInterested)) this.mainPanel.addPoint(timeStamp, price);
    }

    // Return all the records
    public String[] getRecords() {
        return map.values().stream().map(Record::toString).toArray(String[]::new);
    }

}
