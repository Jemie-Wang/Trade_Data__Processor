package com.example.trade_processor;

class Record {
    String symbol;
    int maxTimeGap;
    int lastTime;
    long volume;
    long totalPrice;
    int minPrice;
    int maxPrice;
    Record(int timeStamp, String symbol, int volume, int price){
        this.symbol = symbol;
        // 要考虑超过integer范围的情况吗????
        this.volume = volume;
        this.totalPrice = this.volume * price;
        this.minPrice = price;
        this.maxPrice = price;
        this.maxTimeGap = 0;
        this.lastTime = timeStamp;

    }

    void update(int timeStamp, int currVolume, int price){
        this.volume += currVolume;
        this.totalPrice += currVolume * price;
        this.minPrice = Math.min(price, this.minPrice);
        this.maxPrice = Math.max(price, this.maxPrice);
        this.maxTimeGap = Math.max(timeStamp - this.lastTime, this.maxTimeGap);
        this.lastTime = timeStamp;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        long weightedAveragePrice = this.totalPrice / this.volume;
        sb.append(symbol + ",");
        sb.append(maxTimeGap + ",");
        sb.append(volume + ",");
        sb.append(weightedAveragePrice + ",");
        sb.append(minPrice + ",");
        sb.append(maxPrice + ",");
        return sb.toString();
    }
}
