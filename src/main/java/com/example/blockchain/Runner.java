package com.example.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Runner {

    // Array to store all mining nodes in the network
    private static MiningNode[] miningNodes;

    // Array to store all users transacting
    private static User[] users;

    // Number of nodes in network
    public static int NUMBER_OF_NODES;

    // Transaction per block
    public static int TRANSACTIONS_PER_BLOCK;

    // Number of shards i.e. sub-networks the network is divided into
    public static int NO_OF_SHARDS;

    // Number of times to run, keep a large value to get better idea of average block mining time
    // as time may vary due to outliers in a single run when difficulty is low
    public static int NUMBER_OF_RUNS;

    public static double[] throughput = new double[2];

    public static MerkleTree mt;
    private static final Random random = new Random();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        setup();

        // Configuration for Run
        TRANSACTIONS_PER_BLOCK = 256; // Transactions stored in 1 block
        NUMBER_OF_NODES = 32; // Number of mining nodes in network
        MiningNode.DIFFICULTY = 2; // PoW difficulty, (2 = first 2 characters in block hash will be 0)
        NUMBER_OF_RUNS = 5; // Number runs to perform for a configuration
        NO_OF_SHARDS = 8; // Number of shards the network will be divided into

        // STANDARD RUN WITHOUT SHARDING
        System.out.println("********** TRADITIONAL RUN BEGIN ***********");

        runWithConfigurations(false);

        System.out.println("********** TRADITIONAL RUN ENDS ***********");


        // RUN WITH SHARDING
        System.out.println("********** SHARDING RUN BEGIN ***********");

        System.out.println("Number of Shards: " + NO_OF_SHARDS);
        runWithConfigurations(true);

        System.out.println("********** SHARDING RUN ENDS ***********"+"\n");

        System.out.printf("Performance Improvement %.2fx", (throughput[1] / throughput[0]));

        System.out.println("\n*********************");
    }

    /*
     * Creating 100 users who will be transacting and those
     * transactions will added to the block
     * */
    public static void setup() {
        users = new User[100];
        for (int i = 0; i < users.length; i++) {
            // initializing users with their own public and private keys.
            users[i] = new User(1000.0);
        }
    }

    // creating different mining nodes where
    // each node will be used to mining the incoming block of transaction
    // and update the consensus record for that corresponding block.
    // Initially, the consensus status for all the nodes will be set to false
    // and the merkle root hash will be null
    public static void createMiningThreads() {

        miningNodes = new MiningNode[NUMBER_OF_NODES];
        MiningNode.conensusRecord = new ArrayList<>();
        MiningNode.nonceFound = false;

        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            miningNodes[i] = new MiningNode(i, null);
            MiningNode.conensusRecord.add(false);
        }
    }

    /*
     * Function performs a block mining for all combinations of transactions per block
     * and difficulty.
     * */
    private static void runWithConfigurations(boolean sharding) throws NoSuchAlgorithmException {

        // Sum of all runs
        long sum = 0;

        // We run NUMBER_OF_RUNS times for a particular combination and take average run time
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {

            // Create mining nodes before every run as
            // a thread that is completed cannot be reused
            createMiningThreads();

            // Generate transactions to added to block
            List<Transaction> transactions = generateTransactions();

            // Create merkle tree of the transactions
            mt = new MerkleTree(transactions);
            MiningNode.merkleRootHash = mt.rootHash();

            // Run sharded or standard depending on parameter
            long timeTakeForRun = sharding ? runMiningSharded() : runMining();

            System.out.println("Run Number: " + (i + 1) + " completed in " + timeTakeForRun + " ms");
            sum += timeTakeForRun;
        }

        System.out.println("\n" + "RUN SUMMARY");
        System.out.println("Transactions per block: " + TRANSACTIONS_PER_BLOCK);
        System.out.println("Difficulty: " + MiningNode.DIFFICULTY);
        System.out.println("Total Time: " + sum + " ms");
        System.out.println("Average Run Time: " + sum / 5 + " ms");
        if (sharding) {
            throughput[1] = 1.0 * TRANSACTIONS_PER_BLOCK * 5000L * NO_OF_SHARDS / sum;
            System.out.println("Throughput: " + throughput[1]);
        } else {
            throughput[0] = 1.0 * TRANSACTIONS_PER_BLOCK * 5000L / sum;
            System.out.println("Throughput: " + throughput[0]);
        }
    }

    /*
     * Method to run mining for traditional non-sharded blockchain mining
     * */
    public static long runMining() {

        // Note run start Date for timestamp
        Date start = new Date();

        // Set merkle tree for each mining node
        for (MiningNode mn : miningNodes) {
            mn.setMerkleTree(mt);
            mn.start();
        }

        return executeThreads(start);
    }


    /*
     * Method to run mining for tradition sharded blockchain mining
     * */
    public static long runMiningSharded() {

        // 2D Array to store consensus [Shard id][Node id in shard]
        MiningNode.shardsConsensus = new boolean[NO_OF_SHARDS][NUMBER_OF_NODES / NO_OF_SHARDS];
        MiningNode.shardsNonceFound = new boolean[NO_OF_SHARDS];
        MiningNode.shardsBroadcastBlock = new Block[NO_OF_SHARDS];

        // Note run start Date for timestamp
        Date start = new Date();

        int idx = 0;

        for (MiningNode mn : miningNodes) {
            mn.shardedRun = true;
            mn.shardIndex = idx / (NUMBER_OF_NODES / NO_OF_SHARDS);
            mn.nodeIndex = idx % (NUMBER_OF_NODES / NO_OF_SHARDS);
            mn.setMerkleTree(mt);
            mn.start();
            idx++;
        }

        return executeThreads(start);
    }

    /*
     * Finished executing all threads
     * */
    private static long executeThreads(Date start) {

        //Finish executing all threads before main thread proceeds with next run
        for (MiningNode mn : miningNodes) {
            try {
                mn.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }

        // Note run end Date for timestamp
        Date end = new Date();

        // Return time taken for run
        return end.getTime() - start.getTime();
    }


    /*
     * Function to generate random transactions between users.
     * Used as input to mining node for creating a block.
     * */
    private static List<Transaction> generateTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int u1Idx = random.nextInt(100);
            User u1 = users[u1Idx];
            User u2 = users[(u1Idx + random.nextInt(99)) % 100];
            Date date = new Date();
            byte[] input = ("transaction" + i).getBytes();
            transactions.add(new Transaction(u1, u2.getPubKey().toString(), date.getTime(), random.nextDouble(), input, u1.getPvtKey()));
        }
        return transactions;
    }
}
