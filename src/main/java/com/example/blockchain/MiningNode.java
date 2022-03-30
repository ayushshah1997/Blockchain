package com.example.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MiningNode {

    List<MiningNode> neigbours;
    List<Transaction> trxnBuffer = new ArrayList<Transaction>();
    HashMap<String, Block> hashToBlock = new HashMap<>();

    public static int nonce = 10;
    HashSet<Transaction> seen = new HashSet<>();

    public void setNeighbours(List<MiningNode> neighbours) {
        this.neigbours = neighbours;
    }

    public static void main(String args[]) {}

    public void findNonce() throws NoSuchAlgorithmException {
        long n = 0;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        while(true) {

            n++;
        }
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

    }
}
