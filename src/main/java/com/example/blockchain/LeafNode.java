package com.example.blockchain;

public class LeafNode extends InternalNode {

    private Transaction txn;

    public Transaction getTransaction() {
        return this.txn;
    }

    public void setTransaction(Transaction txn) {
        this.txn = txn;
    }

    public LeafNode(Transaction txn, String hashValue) {
        super();
        this.txn = txn;
    }
}