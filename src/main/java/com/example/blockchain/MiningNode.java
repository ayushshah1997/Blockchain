package com.example.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MiningNode extends Thread {

    private int minerId;
    public static List<Boolean> conensusRecord;
    public static boolean nounceFound = false;
    public MerkleTree mkt;
    public static Block broadcastBlock;

    public MerkleTree getMkt() {
        return mkt;
    }

    public void setMkt(MerkleTree mkt) {
        this.mkt = mkt;
    }

    public MessageDigest digest;
    public static String prevBlockHash;
    public static String merkleRootHash;
    public static int DIFFICULTY = 2;

    public MiningNode(int minerId, MerkleTree mt) {
        this.minerId = minerId;
        this.mkt = mt;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    List<MiningNode> neigbours;
    List<Transaction> trxnBuffer = new ArrayList<>();
    HashMap<String, Block> hashToBlock = new HashMap<>();

    public static int nonce = 10;
    HashSet<Transaction> seen = new HashSet<>();



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
        while (!checkConsensus()) {
            long n = 0;
            while(!nounceFound) {
                String temp = prevBlockHash + merkleRootHash + n;
                byte[] hashValue = new byte[64];
                try {
                    hashValue = digest.digest(temp.getBytes("UTF-16"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(checkByte(hashValue) && !nounceFound){
                    nounceFound = true;
                    broadcastBlock = new Block(merkleRootHash, prevBlockHash, 110001);
                    System.out.println("Nounce found : " +minerId);
                } else {
                    n++;
                }
            }
            // Verify Block
            mkt.validateMerkleHash(mkt.rootNode);
            //System.out.println("Checking Consensus : " +minerId);
            conensusRecord.set(minerId, true);
        }
        //System.out.println("Miner "+minerId);
    }



    public boolean checkConsensus() {
        int count = 0;
        for(boolean b : conensusRecord){
            if(b) count++;
        }
        return count > ( conensusRecord.size() /2);
    }
}
