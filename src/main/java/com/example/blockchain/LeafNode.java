package com.example.blockchain;

public class LeafNode extends InternalNode {

    private Transaction txn;

    public Transaction getTransaction() {
        return this.txn;
    }

    public Transaction getTxn() {
        return txn;
    }

    public void setTxn(Transaction txn) {
        this.txn = txn;
    }

    public LeafNode(Transaction txn, byte[] hashValue) {
        super();
        this.txn = txn;
        this.setHashValue(hashValue);
    }
}