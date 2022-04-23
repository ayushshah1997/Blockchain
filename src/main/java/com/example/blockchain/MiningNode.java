package com.example.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MiningNode extends Thread {

    public static List<Boolean> conensusRecord;
    public static boolean nonceFound = false;
    public static Block broadcastBlock;
    public static Block[] shardsBroadcastBlock;
    public static boolean[][] shardsConsensus;
    public static boolean[] shardsNonceFound;

    private int minerId;
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

    public void standardRun() {
        while (!checkConsensus()) {
            long n = 0;
            while(!nonceFound) {
                String temp = prevBlockHash + merkleRootHash + n;
                byte[] hashValue = new byte[64];
                try {
                    hashValue = digest.digest(temp.getBytes("UTF-16"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(checkByte(hashValue) && !nonceFound) {
                    nonceFound = true;
                    broadcastBlock = new Block(merkleRootHash, prevBlockHash, 110001);
                    System.out.println("Nonce found : " +minerId);
                } else {
                    n++;
                }
            }

            // Verify Block
            merkleTree.validateMerkleHash(merkleTree.rootNode);
            conensusRecord.set(minerId, true);
        }
    }


    public void shardedRun() {
        while (!checkShardConsensus(shardIndex)) {
            long n = 0;
            while(!shardsNonceFound[shardIndex]) {
                String temp = prevBlockHash + merkleRootHash + n;
                byte[] hashValue = new byte[64];
                try {
                    hashValue = digest.digest(temp.getBytes("UTF-16"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(checkByte(hashValue) && !shardsNonceFound[shardIndex]){
                    shardsBroadcastBlock[shardIndex] = new Block(merkleRootHash, prevBlockHash, 110001);
                    shardsNonceFound[shardIndex] = true;
                    System.out.println("Shard Index : " + shardIndex);
                    System.out.println("Nonce found : " +n);
                } else {
                    n++;
                }
            }
            // Verify Block
            merkleTree.validateMerkleHash(merkleTree.rootNode);
            shardsConsensus[shardIndex][nodeIndex] = true;
        }
    }



    public boolean checkConsensus() {
        int count = 0;
        for(boolean b : conensusRecord){
            if(b) count++;
        }
        return count > ( conensusRecord.size() /2);
    }

    public boolean checkShardConsensus(int shardIndex) {
        int count = 0;
        for(boolean b : shardsConsensus[shardIndex]){
            if(b) count++;
        }
        return count > ( shardsConsensus[shardIndex].length/2 );
    }
}
