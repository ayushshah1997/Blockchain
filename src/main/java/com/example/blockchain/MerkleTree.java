package com.example.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MerkleTree {

  private List<Transaction> txnQueue;

  public InternalNode rootNode;

  public List<Transaction> getTxnQueue() {
    return this.txnQueue;
  }

  public void setTxnQueue(List<Transaction> txnQueue) {
    this.txnQueue = txnQueue;
  }

  public MerkleTree(List<Transaction> txnQueue) throws NoSuchAlgorithmException {
    this.txnQueue = txnQueue;
    this.rootNode = createMerkleTree();
  }

  public InternalNode createMerkleTree() throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    int n = this.txnQueue.size();
    InternalNode[] treeNodes = new InternalNode[n];
    for (int i = 0; i < n; i++) {
      byte[] hashValue = digest.digest(
          this.txnQueue.get(i).toString().getBytes(StandardCharsets.UTF_8));
      treeNodes[i] = new LeafNode(this.txnQueue.get(i), hashValue);
    }
    while (n > 1) {
      for (int i = 0; i < n; i += 2) {
        InternalNode leftChild = treeNodes[i];
        InternalNode rightChild = treeNodes[i + 1];
        treeNodes[i / 2] = new InternalNode(
            leftChild,
            rightChild,
            combineTwoHashes(
                leftChild.getHashValue(),
                rightChild.getHashValue(),
                digest));
        leftChild.setParent(treeNodes[i / 2]);
        rightChild.setParent(treeNodes[i / 2]);
      }
      n /= 2;
    }
    return treeNodes[0];
  }

  public boolean validateMerkleHash(InternalNode root) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      if (root.getLeftChild() != null && root.getRightChild() != null) {
        if (root.getHashValue()
            .equals(combineTwoHashes(root.getLeftChild().getHashValue(), root.getRightChild().getHashValue(), digest)
                .toString())) {
          return validateMerkleHash(root.getLeftChild()) && validateMerkleHash(root.getRightChild());
        }
        return false;
      } else {
        LeafNode temp = (LeafNode) root;
        try {
          return temp.getTransaction().verifyDigitalSignature(null, null, null);
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }

    } catch (NoSuchAlgorithmException e) {
      return false;
    }
  }

  private byte[] combineTwoHashes(
      String hash1,
      String hash2,
      MessageDigest digest) {
    return digest.digest((hash1 + hash2).getBytes(StandardCharsets.UTF_8));
  }

  public String rootHash() {
    return "AMDSNJCNDSNNDI";
  }
}
