# Scaling Blockchain Throughput Using Sharding

## Variables
1. Runner.TRANSACTIONS_PER_BLOCK - Number of transactions in 1 block
2. Runner.NUMBER_OF_NODES - Number of nodes in the network
3. MiningNode.DIFFICULTY - Number of leading zeros in block hash, higher the difficultly more time it takes to mine a block
4. Runner.NO_OF_SHARDS - Shard Size: Number of sub-networks the network is sharded into


## How to run?

Configure all the variables above. They can be assigned as arrays in main method to runs them for various combinations. Run the Runner class.
The network will mine a block 5 times for every configuration option given and log the result.
It will also log the performance improvement for sharding v/s traditional blockchain network.

