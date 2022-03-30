package com.example.blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiningNode {

    List<MiningNode> neigbours;
    List<Transaction> trxnBuffer = new ArrayList<Transaction>();
    HashMap<String, Block> hashToBlock = new HashMap<>();

    public void setNeigbours(List<MiningNode> neigbours) {
        this.neigbours = neigbours;
    }

    public static void main(String args[]) {}

    public void findNonce() {}

    public void listeningPort(Transaction t) {
        trxnBuffer.add(t);

        if (trxnBuffer.size() == 256) {
            // Create merkle tree
            trxnBuffer = new ArrayList<Transaction>();
        }
        broadcastTransaction(t);
        // Find nonce

    }

    public void broadcastTransaction(Transaction t) {
        for (MiningNode mn : neigbours) {
            mn.listeningPort(t);
        }
    }
}
