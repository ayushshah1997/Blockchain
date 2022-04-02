package com.example.blockchain;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.util.*;

public class MiningNode {

    List<MiningNode> neigbours;
    List<Transaction> trxnBuffer = new ArrayList<>();
    HashMap<String, Block> hashToBlock = new HashMap<>();

    public static int nonce = 10;
    HashSet<Transaction> seen = new HashSet<>();

    public void setNeighbours(List<MiningNode> neighbours) {
        this.neigbours = neighbours;
    }

    public static void main(String args[]) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        long t = 0;



//        System.out.println(checkByte("00".getBytes("UTF-16"), 4));
//        System.out.println(checkByte("a0".getBytes("UTF-16"), 4));
//        System.out.println(checkByte("0a".getBytes("UTF-16"), 4));
//        System.out.println(checkByte("00".getBytes("UTF-16"), 4));
//        System.out.println(checkByte("00a".getBytes("UTF-16"), 4));
//        System.out.println(checkByte("00b".getBytes("UTF-16"), 4));


        for (int i =0; i < 30; i++) {
            t += findNonce("c7b6r87r487r487", "ewcy32i8y4398tvby89c4n23");
            System.out.println(t);
        }
        System.out.println("Time taken: " + t/30);
    }

    public static long findNonce(String prevHash, String merkleRootHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        long tStart = new Date().getTime();
        long n = 0;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

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
            //System.out.println(buffer[i]);
            if( buffer[i] != 48 ) {
                rtn = false;
                break;
            }
        }
        return rtn;
    }

    public void listeningPort(Transaction t) {

        if(!seen.contains(t)){
            trxnBuffer.add(t);
        }

        if (trxnBuffer.size() == 256) {
            // Create merkle tree
            MerkleTree mt = new MerkleTree(trxnBuffer);
            trxnBuffer = new ArrayList<>();
        }
        broadcastTransaction(t);
        // Find nonce

    }

    public void broadcastTransaction(Transaction t) {
        for (MiningNode mn : neigbours) {
            mn.listeningPort(t);
        }
    }

    public void listenBlock(Block b){

    }

    public void broadcastBlock(Block b) {
        for (MiningNode mn : neigbours) {
            mn.listenBlock(b);
        }
    }
}
