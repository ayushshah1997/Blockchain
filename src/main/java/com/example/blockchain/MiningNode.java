package com.example.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MiningNode extends Thread {

    private String minerId;
    public static List<Boolean> conensusRecord = new ArrayList<>();
    public static boolean nounceFound = false;
    public static Block broadcastBlock;
    public static MessageDigest digest;
    public static String prevBlockHash;
    public static String merkleRootHash;

    public MiningNode(String minerId) {
        this.minerId = minerId;
    }

    static {
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


    public static void main(String args[]) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        long t = 0;

        for (int i =0; i < 30; i++) {
            t += findNonce("c7b6r87r487r487", "ewcy32i8y4398tvby89c4n23");
            System.out.println(t);
        }
        System.out.println("Time taken: " + t/30);
    }

    public static long findNonce(String prevHash, String merkleRootHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        long tStart = new Date().getTime();
        long n = 0;


        while(true) {
            String temp = prevHash + merkleRootHash + n;
            byte[] hashValue = digest.digest(temp.getBytes("UTF-16"));

            if(checkByte(hashValue, 3)){
              break;
            } else {
                n++;
            }
        }
        long tEnd = new Date().getTime();
        return tEnd - tStart;

        //Block b = new Block();
        //broadcastBlock(b);
    }


    public static boolean checkByte(byte[] buffer, int nonce) {
        boolean rtn = true;
        for(int i =0; i < nonce; i++) {
            if( buffer[i] != 48 ) {
                rtn = false;
                break;
            }
        }
        return rtn;
    }

    public void listeningPort(Transaction t) throws NoSuchAlgorithmException {

        if(!seen.contains(t)){
            trxnBuffer.add(t);
        }

        if (trxnBuffer.size() == 256) {
            // Create merkle tree
            MerkleTree mt = new MerkleTree(trxnBuffer);
            trxnBuffer = new ArrayList<>();
        }
        //broadcastTransaction(t);
        // Find nonce

    }

//    public void broadcastTransaction(Transaction t) {
//        for (MiningNode mn : neigbours) {
//            mn.listeningPort(t);
//        }
//    }
//
//    public void listenBlock(Block b){
//
//    }
//
//    public void broadcastBlock(Block b) {
//        for (MiningNode mn : neigbours) {
//            mn.listenBlock(b);
//        }
//    }

    @Override
    public void run() {

        while (!checkConsensus()) {
            while(!nounceFound) {

            }
        }
    }



    public boolean checkConsensus() {
        int count = 0;
        for(boolean b : conensusRecord){
            if(b) count++;
        }
        return count>50;
    }
}
