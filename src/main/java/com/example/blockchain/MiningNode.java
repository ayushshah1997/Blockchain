package com.example.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MiningNode extends Thread {

    // Stores consensus status of all nodes in the network for traditional blockchain
    public static List<Boolean> conensusRecord;

    // Set true if one of the nodes has solved the hash problem
    public static boolean nonceFound = false;

    // Block broadcast by node that finds nonce
    public static Block broadcastBlock;

    // Block broadcast by nodes in a sharded blockchain
    public static Block[] shardsBroadcastBlock;

    // Stores consensus record in a sharded network
    public static boolean[][] shardsConsensus;

    // Stores if nonce found for shards
    public static boolean[] shardsNonceFound;

    private final int minerId;
    public MerkleTree merkleTree;
    public boolean shardedRun = false;
    public int shardIndex;
    public int nodeIndex;

    public MerkleTree getMerkleTree() {
        return merkleTree;
    }

    public void setMerkleTree(MerkleTree mkt) {
        this.merkleTree = mkt;
    }

    public MessageDigest digest;
    public static String prevBlockHash = "fa7e972c41aaf15c57925a21dbcdb1525ab522f6b4b915099dbd8c0c340d8b85";
    public static String merkleRootHash;
    public static int DIFFICULTY = 2;

    public MiningNode(int minerId, MerkleTree mt) {
        this.minerId = minerId;
        this.merkleTree = mt;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /*
    * Function checks if byte buffer passed matches difficulty requirement
    * */
    public static boolean checkByte(byte[] buffer) {
        boolean rtn = true;
        for(int i =0; i < DIFFICULTY; i++) {
            if( buffer[i] != 48 ) {
                rtn = false;
                break;
            }
        }
        return rtn;
    }

    @Override
    public void run() {
        if(shardedRun) {
            shardedRun();
        } else {
            standardRun();
        }
    }

    /*
    * Standard non-sharded run
    * */
    public void standardRun() {
        // Run loop while consensus hasn't been reached
        while (!checkConsensus()) {

            // Trial value for nonce
            long trialNonce = 0;

            // Keep trying values to for nonce till no other node has found it
            while(!nonceFound) {

                // Concatenate previous block hash, merkle tree root hash and trial nonce
                String temp = prevBlockHash + merkleRootHash + trialNonce;
                byte[] hashValue = new byte[64];

                try {
                    // Find hash value for temp
                    hashValue = digest.digest(temp.getBytes("UTF-16"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Check if hash value generated above matches difficulty criteria and
                // no other node has yet found it
                if(checkByte(hashValue) && !nonceFound) {
                    nonceFound = true;
                    // Set block to broadcast
                    broadcastBlock = new Block(merkleRootHash, prevBlockHash, new Date().getTime());
                } else {
                    trialNonce++;
                }
            }

            // Verify Block
            merkleTree.validateMerkleHash(merkleTree.rootNode);
            // Set consensus
            conensusRecord.set(minerId, true);
        }
    }


    public void shardedRun() {
        // Run loop while consensus hasn't been reached in shard
        while (!checkShardConsensus(shardIndex)) {

            // Trial value for nonce
            long trialNonce = 0;


            // Keep trying values to find nonce till no other node has found it
            while(!shardsNonceFound[shardIndex]) {

                // Concatenate previous block hash, merkle tree root hash and trial nonce
                String temp = prevBlockHash + merkleRootHash + trialNonce;

                byte[] hashValue = new byte[64];
                try {
                    // Find hash value for temp
                    hashValue = digest.digest(temp.getBytes("UTF-16"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Check if hash value generated above matches difficulty criteria and
                // no other node in shard has yet found it
                if(checkByte(hashValue) && !shardsNonceFound[shardIndex]){
                    shardsBroadcastBlock[shardIndex] = new Block(merkleRootHash, prevBlockHash, 110001);
                    shardsNonceFound[shardIndex] = true;
                    // System.out.println("Shard Index : " + shardIndex);
                    // System.out.println("Nonce found : " +trialNonce);
                } else {
                    trialNonce++;
                }
            }
            // Verify Block
            merkleTree.validateMerkleHash(merkleTree.rootNode);
            // Set consensus
            shardsConsensus[shardIndex][nodeIndex] = true;
        }
    }



    /*
    * Check consensus record, returns true if > 51%
    * nodes have voted true
    * */
    public boolean checkConsensus() {
        int count = 0;
        for(boolean b : conensusRecord){
            if(b) count++;
        }
        return count > ( conensusRecord.size() /2);
    }

    /*
     * Check consensus record, returns true if > 51%
     * nodes have voted true for that shardIndex
     * */
    public boolean checkShardConsensus(int shardIndex) {
        int count = 0;
        for(boolean b : shardsConsensus[shardIndex]){
            if(b) count++;
        }
        return count > ( shardsConsensus[shardIndex].length/2 );
    }
}
