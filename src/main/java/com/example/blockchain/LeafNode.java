package com.example.blockchain;

public class LeafNode extends InternalNode {
    private InternalNode parent;
    private String senderAddress;
    private String payeeAddress;
    private String hashValue;
    private double amount;
    private byte[] digitalSignature;

    public LeafNode(String senderAddress, String payeeAddress, double amount, byte[] digitalSignature) {
        super();
        this.senderAddress = senderAddress;
        this.payeeAddress = payeeAddress;
        this.amount = amount;
        this.digitalSignature = digitalSignature;
    }

    public InternalNode getParent() {
        return this.parent;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public String getSenderAddress() {
        return this.senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getPayeeAddress() {
        return this.payeeAddress;
    }

    public void setPayeeAddress(String payeeAddress) {
        this.payeeAddress = payeeAddress;
    }

    public String getHashValue() {
        return this.hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public byte[] getDigitalSignature() {
        return this.digitalSignature;
    }

    public void setDigitalSignature(byte[] digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

}
