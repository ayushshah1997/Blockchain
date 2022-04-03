package com.example.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Runner {


    // Google Doc link
    // https://docs.google.com/document/d/1Z2VluX6tIUAeKA0D601h_HusLjENXPa9lT1BdDDOWZU/edit?usp=sharing

    private static MiningNode[] miningNodes;

    private static User[] users;

    private static Random r = new Random();
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

        miningNodes = new MiningNode[100];

        for (int i = 0; i < 100; i++) {
            miningNodes[i] = new MiningNode(i+"");
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        setup();
        runMining();
    }

    // Generate transaction at fixed interval - Done
    // Pass transaction via gossip to nodes
    // Multithread and nodes process transaction
    // Monitor growth in blockchain size
    public static void runMining() throws NoSuchAlgorithmException {
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
        for( MiningNode mn: miningNodes) {
            mn.start();
        }

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
