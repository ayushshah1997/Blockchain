package com.example.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Runner {

    // Array to store all mining nodes in the network
    private static MiningNode[] miningNodes;

    // Array to store all users transacting
    private static User[] users;

    // Number of nodes in network
    public static int numOfNodes = 32;

    // Transaction per block
    public static int trxnsPerBlock;

    // Number of shards i.e. sub-networks the network is divided into
    public static int NO_OF_SHARDS;

    public static MerkleTree mt;
    private static final Random random = new Random();
    private static String csv = "";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        setup();

        // Configuration for Run
        int[] difficulty = {2};
        int[] trxns = {256};
        int[] shardSize = {2};

        // STANDARD RUN WITHOUT SHARDING
        System.out.println("********** TRADITIONAL RUN BEGIN ***********");
        runForTrxnSize(trxns,difficulty, false);

        System.out.println("Trxns per block,Difficulty, No. of Runs, Total Time");
        System.out.println(csv+"\n");
        System.out.println("********** TRADITIONAL RUN ENDS ***********");

        csv = "";

        System.out.println("********** SHARDING RUN BEGIN ***********");
        // RUN WITH SHARDING
        for( int s : shardSize) {
            NO_OF_SHARDS = s;
            runForTrxnSize(trxns, difficulty, true);
        }

        System.out.println("Trxns per block,Difficulty,No. of Shards, No. of Runs, Total Time");
        System.out.println(csv+"\n");
        System.out.println("********** SHARDING RUN ENDS ***********");

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

        miningNodes = new MiningNode[numOfNodes];
        MiningNode.conensusRecord = new ArrayList<>();
        MiningNode.nonceFound = false;

        for (int i = 0; i < numOfNodes; i++) {
            miningNodes[i] = new MiningNode(i, null);
            MiningNode.conensusRecord.add(false);
        }
    }

    /*
    * Function performs a block mining for all combinations of transactions per block
    * and difficulty.
    * */
    private static void runForTrxnSize(int[] transactionsPerBlock, int[] difficulty,  boolean sharding) throws NoSuchAlgorithmException {

        for(int d : difficulty){
            MiningNode.DIFFICULTY = d;
            for(int t : transactionsPerBlock) {

                trxnsPerBlock = t;
                long sum = 0;

                // We run 5 time for a particular combination and take average run time
                int numOfRuns = 5;

                for(int i =0; i < numOfRuns; i++) {

                    // Create mining nodes before every run as
                    // a thread that is completed cannot be reused
                    createMiningThreads();

                    System.out.println("Run Number: " + i);

                    // Generate transactions to added to block
                    List<Transaction> transactions = generateTransactions();

                    // Create merkle tree of the transactions
                    mt = new MerkleTree(transactions);
                    MiningNode.merkleRootHash = mt.rootHash();

                    // Run sharded or standard depending on parameter
                    sum += sharding ? runMiningSharded() : runMining();
                }


                // Logging run values to csv format
                if(sharding ){
                    String temp = t +"," + MiningNode.DIFFICULTY+ "," + NO_OF_SHARDS +","+ numOfRuns  +"," + sum;
                    csv +=  temp + "\n";
                    System.out.println(temp);
                } else {
                    String temp = t +"," + MiningNode.DIFFICULTY+ "," + numOfRuns  +"," + sum;
                    csv += temp + "\n";
                    System.out.println(temp);
                }

            }
        }
    }

    /*
    * Method to run mining for traditional non-sharded blockchain mining
    * */
    public static long runMining() {

        // Note run start Date for timestamp
        Date start = new Date();

        // Set merkle tree for each mining node
        for( MiningNode mn: miningNodes) {
            mn.setMerkleTree(mt);
            mn.start();
        }

        return  executeThreads(start);
    }


    /*
     * Method to run mining for tradition sharded blockchain mining
     * */
    public static long runMiningSharded() {

        // 2D Array to store consensus [Shard id][Node id in shard]
        MiningNode.shardsConsensus = new boolean[NO_OF_SHARDS][numOfNodes/NO_OF_SHARDS];
        MiningNode.shardsNonceFound = new boolean[NO_OF_SHARDS];
        MiningNode.shardsBroadcastBlock = new Block[NO_OF_SHARDS];

        // Note run start Date for timestamp
        Date start = new Date();
        
        int idx = 0;

        for( MiningNode mn: miningNodes) {
            mn.shardedRun = true;
            mn.shardIndex = idx/(numOfNodes/NO_OF_SHARDS);
            mn.nodeIndex = idx%(numOfNodes/NO_OF_SHARDS);
            mn.setMerkleTree(mt);
            mn.start();
            idx++;
        }

        return  executeThreads(start);
    }

    private static long executeThreads(Date start) {

        //Finish executing all threads before main thread proceeds with next run
        for (MiningNode mn: miningNodes) {
            try {
                mn.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }

        // Note run end Date for timestamp
        Date end = new Date();


        // Return time taken for run
        return end.getTime()-start.getTime();
    }


    /*
    * Function to generate random transactions between users.
    * Used as input to mining node for creating a block.
    * */
    private static List<Transaction> generateTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for(int i=0; i<4; i++){
            int u1Idx = random.nextInt(100);
            User u1 = users[u1Idx];
            User u2 = users[(u1Idx + random.nextInt(99)) % 100];
            Date date = new Date();
            byte[] input=("transaction"+i).getBytes();
            transactions.add(new Transaction(u1, u2.getPubKey().toString(), date.getTime(), random.nextDouble(),input,u1.getPvtKey()));
        }
        return transactions;
    }
}
