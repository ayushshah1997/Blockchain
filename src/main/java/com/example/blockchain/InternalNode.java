package com.example.blockchain;

public class InternalNode {

    private InternalNode parent;
    private InternalNode leftChild;
    private InternalNode rightChild;
    private byte[] hashValue;

    public InternalNode() {
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.hashValue = null;
    }

    public InternalNode(
            InternalNode leftChild,
            InternalNode rightChild,
            byte[] hashValue
    ) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.hashValue = hashValue;
    }

    public InternalNode getParent() {
        return this.parent;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public InternalNode getLeftChild() {
        return this.leftChild;
    }

    public void setLeftChild(InternalNode leftChild) {
        this.leftChild = leftChild;
    }

    public InternalNode getRightChild() {
        return this.rightChild;
    }

    public void setRightChild(InternalNode rightChild) {
        this.rightChild = rightChild;
    }

    public String getHashValue() {
        return new String(this.hashValue);
    }

    public void setHashValue(byte[] hashValue) {
        this.hashValue = hashValue;
    }
}

