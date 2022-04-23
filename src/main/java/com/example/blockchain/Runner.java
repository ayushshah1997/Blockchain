package com.example.blockchain;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.*;

public class Runner {

    private static MiningNode[] miningNodes;
    private static User[] users;
    public static int numOfNodes = 32;
    public static int trxnsPerBlock;
    public static int NO_OF_SHARDS;
    public static MerkleTree mt;
    private static final Random random = new Random();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        setup();

        // Table 1
        MiningNode.DIFFICULTY = 2;
        int[] trxns = {128,256,1024};

        NO_OF_SHARDS = 4;

        // STANDARD RUN WITHOUT SHARDING
        runForTrxnSize(trxns, false);

        // RUN WITH SHARDING
        runForTrxnSize(trxns, true);

    }

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



    private static void runForTrxnSize(int[] trxns, boolean sharding) throws NoSuchAlgorithmException {
        for(int t : trxns) {
            trxnsPerBlock = t;
            long sum = 0;
            int numOfRuns = 5;
            for(int i =0; i < numOfRuns; i++) {
                createMiningThreads();
                System.out.println("Run Number: " + i);
                List<Transaction> transactions = generateTransactions();
                mt = new MerkleTree(transactions);
                MiningNode.merkleRootHash = mt.rootHash();

                sum += sharding ? runMiningSharded() : runMining();
            }

            System.out.println("********** RUN SUMMARY ***********");
            System.out.println("Transactions per block: "+ t);
            System.out.println("Difficulty: " + MiningNode.DIFFICULTY);
            System.out.println("Total time taken for " + numOfRuns +"  runs: " + sum + " ms" );
            System.out.println("************************************");
        }
    }

    // Generate transaction at fixed interval - Done
    // Pass transaction via gossip to nodes
    // Multithread and nodes process transaction
    // Monitor growth in blockchain size
    public static long runMining() throws NoSuchAlgorithmException {
        Date start = new Date();
        for( MiningNode mn: miningNodes) {
            mn.setMerkleTree(mt);
            mn.start();
        }

        return  joinThreads(start);
    }


    public static long runMiningSharded() {

        MiningNode.shardsConsensus = new boolean[NO_OF_SHARDS][numOfNodes/NO_OF_SHARDS];
        MiningNode.shardsNonceFound = new boolean[NO_OF_SHARDS];
        MiningNode.shardsBroadcastBlock = new Block[NO_OF_SHARDS];

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

        return  joinThreads(start);
    }

    private static long joinThreads(Date start) {
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
        // System.out.println("Time Taken: " +  dt/1000 + " seconds");
        return dt;
    }


    private static List<Transaction> generateTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for(int i=0; i<256; i++){
            int u1Idx = random.nextInt(100);
            User u1 = users[u1Idx];
            User u2 = users[(u1Idx + random.nextInt(99)) % 100];
            Date date = new Date();
            byte[] input=("transaction"+i).getBytes();
            PrivateKey privKey=u1.getPvtKey();
            transactions.add(new Transaction(u1, u2.getPubKey().toString(), date.getTime(), random.nextDouble(),input,privKey));
        }
        return transactions;
    }
}
