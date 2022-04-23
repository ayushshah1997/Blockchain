package com.example.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Runner {


    // Google Doc link
    // https://docs.google.com/document/d/1Z2VluX6tIUAeKA0D601h_HusLjENXPa9lT1BdDDOWZU/edit?usp=sharing

    private static MiningNode[] miningNodes;

    private static User[] users;

    public static int numOfNodes = 16;
    public static int trxnsPerBlock = 128;

    private static final Random r = new Random();
    /*
     * Runs:
     * 1. Without Sharding Block Capacity - 256 txns
     * 2. Without Sharding Block Capacity - 2048 txns
     * 3. With Sharding Block Capacity - 256 txns
     * 4. Without Sharding Block Capacity - 2048 txns
     */

    // Create Nodes - Done
    // Create Users
    public static void setup() {

        users = new User[100];

        for (int i = 0; i < users.length; i++) {
            users[i] = new User(1000.0);
        }


        miningNodes = new MiningNode[numOfNodes];

        MiningNode.conensusRecord = new ArrayList<>();
        for (int i = 0; i < numOfNodes; i++) {
            miningNodes[i] = new MiningNode(i, null);
            MiningNode.conensusRecord.add(false);
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        setup();
        System.out.println("********* RUN WITHOUT SHARDING *************");

        // Table 1
        MiningNode.DIFFICULTY = 2;
        int[] trxns = {256};//{128,256,1024};

        runForTrxnSize(trxns);

        /*
        MiningNode.DIFFICULTY = 3;

        runForTrxnSize(trxns);

        System.out.println("********* RUN SHARDING *************");

        int[] numOfShards = {8,4,2};

        for(int ns : numOfShards){
            MiningNode.DIFFICULTY = 2;
            numOfNodes = 32/ns;
            for(int t : trxns) {
                trxnsPerBlock = t;
                long sum = 0;
                for(int i =0; i < 5; i++){
                    System.out.println("Run Number: " + i);
                    sum += runMining();
                }
                System.out.println("********** RUN SUMMARY ***********");
                System.out.println("Number of shards: "+ ns );
                System.out.println("Transactions per block: "+ t  );
                System.out.println("Difficulty: " + MiningNode.DIFFICULTY);
                System.out.println("Total time taken for 5 runs: " + sum + " ms" );
            }

            MiningNode.DIFFICULTY = 3;
            numOfNodes = 32/ns;
            for(int t : trxns) {
                trxnsPerBlock = t;
                long sum = 0;
                for(int i =0; i < 5; i++){
                    System.out.println("Run Number: " + i);
                    sum += runMining();
                }
                System.out.println("********** RUN SUMMARY ***********");
                System.out.println("Number of shards: "+ ns );
                System.out.println("Transactions per block: "+ t  );
                System.out.println("Difficulty: " + MiningNode.DIFFICULTY);
                System.out.println("Total time taken for 5 runs: " + sum + " ms" );
            }
        }

        */
    }

    private static void runForTrxnSize(int[] trxns) throws NoSuchAlgorithmException {
        for(int t : trxns) {
            trxnsPerBlock = t;
            long sum = 0;
            //for(int i =0; i < 5; i++){
                //System.out.println("Run Number: " + i);
                sum += runMining();
            //}
            System.out.println("********** RUN SUMMARY ***********");
            System.out.println("Transactions per block: "+ t  );
            System.out.println("Difficulty: " + MiningNode.DIFFICULTY);
            System.out.println("Total time taken for 1 run: " + sum + " ms" );
        }
    }

    // Generate transaction at fixed interval - Done
    // Pass transaction via gossip to nodes
    // Multithread and nodes process transaction
    // Monitor growth in blockchain size
    public static long runMining() throws NoSuchAlgorithmException {
        List<Transaction> trxns = new ArrayList<>();
        for(int i=0; i<256; i++){
            int u1Idx = r.nextInt(100);
            User u1 = users[u1Idx];
            User u2 = users[(u1Idx + r.nextInt(99)) % 100];
            Date date = new Date();
            trxns.add(new Transaction(u1, u2.getPubKey().toString(), date.getTime(), r.nextDouble()));
        }
        String  prevBlockHash = "aaavnuvnreunr98n89f34nCN(E#E#E(";
        MerkleTree mt = new MerkleTree(trxns);
        MiningNode.prevBlockHash = prevBlockHash;
        MiningNode.merkleRootHash = mt.rootHash();
        Date start = new Date();
        for( MiningNode mn: miningNodes) {
            mn.setMkt(mt);
            mn.start();
        }

        for (MiningNode mn: miningNodes) {
            try {
                mn.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }

        Date end = new Date();

        long dt = end.getTime()-start.getTime();
        System.out.println("Number of nodes in network: " + numOfNodes);
        System.out.println("Time Taken: " +  dt/1000 + " seconds");

        return dt;

    }



    /*
     * Node Class
     * Unique Address
     * Find nonce
     *
     */

    /*
     * Sharded Run
     *
     * Divide into sub network
     *
     */


    public static void runMiningSharded() {

    }
}
