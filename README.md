# Scaling Blockchain Throughput Using Sharding

## Introduction

This project code simulates the mining of a block. We mine blocks for a given configuration using two methods:
1. Traditional Mining Network
2. Sharded Mining Network
The run summaries for both are logged along with the performance improvement in throughput (Transactions processed per second) for the sharded network.

## Variables to configure:
In the *main* method of *Runner* class configure the following variables:
1. Runner.TRANSACTIONS_PER_BLOCK - Number of transactions in 1 block
2. Runner.NUMBER_OF_NODES - Number of nodes in the network
3. MiningNode.DIFFICULTY - Number of leading zeros in block hash, higher the difficultly more time it takes to mine a block
4. Runner.NUMBER_OF_RUNS - Number of times to run the mining process for a method
5. Runner.NO_OF_SHARDS - Shard Size: Number of sub-networks the network is sharded into


## How to run?
Configure all the variables above in the main method of Runner class. Run the Runner class.
The network will mine a block NUMBER_OF_RUNS times for every configuration option given and log the result.
It will also log the performance improvement for sharding v/s traditional blockchain network.

## Sample Run:

### Variables Configured *Runner.main()*:
```
TRANSACTIONS_PER_BLOCK = 256; // Transactions stored in 1 block
NUMBER_OF_NODES = 32; // Number of mining nodes in network
MiningNode.DIFFICULTY = 2; // PoW difficulty, (2 = first 2 characters in block hash will be 0)
NUMBER_OF_RUNS = 5; // Number runs to perform for a configuration
NO_OF_SHARDS = 4; // Number of shards the network will be divided into
```

### Run Logs:
```
********** TRADITIONAL RUN BEGIN ***********
Run Number: 1 completed in 631 ms
Run Number: 2 completed in 602 ms
Run Number: 3 completed in 627 ms
Run Number: 4 completed in 278 ms
Run Number: 5 completed in 412 ms

RUN SUMMARY
Transactions per block: 256
Difficulty: 2
Total Time: 2550 ms
Average Run Time: 510 ms
Throughput: 501.96078431372547
********** TRADITIONAL RUN ENDS ***********

********** SHARDING RUN BEGIN *************
Number of Shards: 4
Run Number: 1 completed in 658 ms
Run Number: 2 completed in 495 ms
Run Number: 3 completed in 506 ms
Run Number: 4 completed in 509 ms
Run Number: 5 completed in 411 ms

RUN SUMMARY
Transactions per block: 256
Difficulty: 2
Total Time: 2579 ms
Average Run Time: 515 ms
Throughput: 1985.2656068243505
********** SHARDING RUN ENDS ***********

Performance Improvement 3.96x
```