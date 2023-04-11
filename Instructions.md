# Programming Exercise
Here is the scenario for this exercise:

Your manager enters your office and tells you that there is a quick job
that needs to be done.  

One of the clients has asked for a statistical summary of data that is 
gathered every day from the stock market.  Not only will you need to develop 
the code to create the output but also set up the jobs to execute and monitor 
the process running on a daily basis at the market close (4:00 EST).

As you have a lot on your plate she tells you to not spend more than an hour 
on this task.  The client will be looking for an easily
maintainable solution that consistently produces the correct results.

The requirements for the outputs as well as the input file definition are 
outlined below.

# Requirements

Input:
The input file represents a very simplified stream of trades on an exchange.  
Each row represents a trade.  If you don't know what that means don't worry.  
The data can be thought of as a time series of values in columns: 

```
<TimeStamp>,<Symbol>,<Quantity>,<Price>
```

Although the provided input file is small, the solution should be able to handle 
a source dataset well beyond the amount memory and hard disk space on your machine.

## Definitions

- TimeStamp is value indicating the microseconds since midnight.
- Symbol is the 3 character unique identifier for a financial instrument (Stock, future etc.)
- Quantity is the amount traded (shares, contracts etc.)
- Price is the price of the trade for that financial instrument.

## Safe Assumptions:

- TimeStamp is always for the same day and won't roll over midnight.
- TimeStamp is increasing or same as previous trade (time gap will never be < 0).
- Price - our currency is an integer based currency.  No decimal points.
- Price - Price is always > 0.

*Example*: here is a row for a trade of 10 shares (quantity) of AAA stock at a price of 12 
```
1234567,AAA,10,12
```

## Problem:

Find the following on a per symbol basis:
- Maximum time gap
  (time gap = Amount of time that passes between consecutive trades of a symbol)
  if only 1 trade is in the file then the gap is 0.
- Total Volume traded (Sum of the quantity for all trades in a symbol).
- Weighted Average Price.  Average price per unit traded not per trade.
- Min Trade Price.
- Max Trade Price.
  Result should be truncated to whole numbers.

  Example:
```
For the following trades:
    20 shares (quantity) of AAA @ 18
    5 shares (quantity) of AAA @ 7

    input rows would be:
    1001,AAA,20,18
    1002,AAA,5,7

    Weighted Average Price = ((20 * 18) + (5 * 7)) / (20 + 5) = 15
```

### Output:

Your solution should produce a file called 'output.csv'.
file should be a comma separate file with this format:

```
<symbol>,<MaxTimeGap>,<Volume>,<WeightedAveragePrice>,<MinPrice>.<MaxPrice>
```

The output should be sorted by symbol ascending ('AAA' should be first).

Sample Input:
```
52924702,AAA,13,1136
52924702,AAC,20,477
52925641,AAB,31,907
52927350,AAB,29,724
52927783,AAC,21,638
52930489,AAA,18,1222
52931654,AAA,9,1077
52933453,AAB,9,756
```

Sample Output (given the above input):
```
AAA,5787,40,1161,1077,1222
AAB,6103,69,810,724,907
AAC,3081,41,559,477,638
```

# Constraints:

Preferred solutions will be done in C++ or another compiled language

Memory is limited on the machine processing the data - the entire file can't be held in memory.

Only use the toolset provided in the base install of your language/platform.
No add on modules/libraries should be used in your solution.

Your solution should read from stdin and write to stdout.  In order to create
the output file as requested you would use the following syntax if your solution
were named 'process_trades'.

```
process_trades < input.csv > output.csv
```


# Data Visualization

For this portion feel free to use any toolset/language you are familiar with.

Create an image file (png or jpg) that shows the price change over time for the AAA symbol. Timestamp will be on the X axis and price on the Y axis.

label the file AAA.png or AAA.jpg as appropriate.
Include this file in your submitted results. 

# Submitting Results

Please submit the following in a zip file.  

- Your source code.
- The output.csv file produced from the input.csv provided.
- The image file you generated (e.g. AAA.png)
- A text document where you answer the following questions:
   - How you verified/tested your solution.
   - Your thoughts on how you would set up the task to run on a daily basis.
   - Your thoughts on how you would monitor the ongoing execution of your task.
   - Include the amount of time you spent on the solution.
   - Include your thoughts on why you chose your OS/Language/tools.

Your submission will be evaluated on the following criteria:

1. Correctness of the output.
2. Cleanliness and maintainability of the solution.

This is intended to be a simple exercise and shouldn't take more than an hour
to complete.  We take that into account when we evaluate.

Please return your solution within 48 hours of receiving the test.

Good luck!

_All aspects of this test (problem, instructions and data) are copyright 2023 by Quantlab Financial LLC.  Please don't publish or make public any portion of this test or your solution._