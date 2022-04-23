package com.example.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MerkleTree {

  private List<Transaction> txnQueue;

  public InternalNode rootNode;
//
//  public List<Transaction> getTxnQueue() {
//    return this.txnQueue;
//  }
//
//  public void setTxnQueue(List<Transaction> txnQueue) {
//    this.txnQueue = txnQueue;
//  }

  public MerkleTree(List<Transaction> txnQueue) throws NoSuchAlgorithmException {
    this.txnQueue = txnQueue;
    this.rootNode = createMerkleTree();
  }

  public InternalNode createMerkleTree() throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    int n = this.txnQueue.size();

    // Each transaction will be linked to their corresponding parent (internal) node
    InternalNode[] treeNodes = new InternalNode[n];
    for (int i = 0; i < n; i++) {

      //generating hash value for each transaction
      byte[] hashValue = digest.digest(
          this.txnQueue.get(i).toString().getBytes(StandardCharsets.UTF_8));

      // initializing leaf nodes with their corresponding transaction and a hash value
      treeNodes[i] = new LeafNode(this.txnQueue.get(i), hashValue);
    }

    // creating the merkle tree with transactions as the leaf nodes and all the
    // internal nodes as the parent nodes. Finally, the root node will be returned which will
    // store the addresses of its child nodes. To visualize, we are building merkle tree
    // in the bottom-up fashion starting with storing the transactions as the leaf nodes and travelling
    // to the top returning the root.
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

      //checking if there are child nodes to be validated
      if (root.getLeftChild() != null && root.getRightChild() != null) {
        if (root.getHashValue()
            .equals(combineTwoHashes(root.getLeftChild().getHashValue(), root.getRightChild().getHashValue(), digest)
                .toString())) {

          //recursive call to validateMerkleHash with left and right child as the root node
          return validateMerkleHash(root.getLeftChild()) && validateMerkleHash(root.getRightChild());
        }
        return false;
      } else {

        // at this stage we have found a leaf node since there are no left or right child
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

  // this method combines the hash values of two internal nodes
  // and returns the hash value of the parent node
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
