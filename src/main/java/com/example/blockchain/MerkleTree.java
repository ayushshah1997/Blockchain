package com.example.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

  private MessageDigest digest = MessageDigest.getInstance("SHA-256");
  private List<Transaction> txnQueue;

  public List<Transaction> getTxnQueue() {
    return this.txnQueue;
  }

  public void setTxnQueue(List<Transaction> txnQueue) {
    this.txnQueue = txnQueue;
  }

  public MerkleTree(List<Transaction> txnQueue) {
    this.txnQueue = txnQueue;
  }

  public InternalNode createMerkleTree() {
    int n = this.txnQueue.size();
    InternalNode[] treeNodes = new InternalNode[n];
    for (int i = 0; i < n; i++) {
      byte[] hashValue = digest.digest(
        this.txnQueue.get(i).toString().getBytes(StandardCharsets.UTF_8)
      );
      treeNodes[i] = new LeafNode(this.txnQueue.get(i), hashValue);
    }
    while (n > 1) {
      for (int i = 0; i < n; i += 2) {
        InternalNode leftChild = treeNodes[i];
        InternalNode rightChild = treeNodes[i + 1];
        treeNodes[i / 2] =
          new InternalNode(
            leftChild,
            rightChild,
            combineTwoHashes(
              leftChild.getHashValue(),
              rightChild.getHashValue()
            )
          );
        leftChild.setParent(treeNodes[i / 2]);
        rightChild.setParent(treeNodes[i / 2]);
      }
      n /= 2;
    }
    return treeNodes[0];
  }

  private byte[] combineTwoHashes(byte[] hash1, byte[] hash2) {
    return digest.digest(
      (hash1.toString() + hash2.toString()).getBytes(StandardCharsets.UTF_8)
    );
  }
}
