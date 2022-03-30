package com.example.blockchain;

public class InternalNode {
    private InternalNode parent;
    private InternalNode leftChild;
    private InternalNode rightChild;
    private String hashValue;

    public InternalNode() {
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.hashValue = "";
    }

    public InternalNode(
            InternalNode parent,
            InternalNode leftChild,
            InternalNode rightChild,
            String hashValue) {
        this.parent = parent;
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
        return this.hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }
}
