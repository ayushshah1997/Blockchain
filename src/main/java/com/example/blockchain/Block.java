package com.example.blockchain;

import java.security.MessageDigest;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private long nonceValue;
    private MerkleTree merkleTree;
    private String merkleRootHash;
    private long timeStamp;
    private int nonce=10;

    public Block(String merkleRootHash, String previousHash, long timeStamp) {
        this.merkleRootHash = merkleRootHash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        hash=calculateBlockHash();
    }

    public String calculateBlockHash() {
        try {
            String dataToHash = previousHash
                    + Long.toString(timeStamp)
                    + Integer.toString(nonce)
                    + data;
            MessageDigest digest = null;
            byte[] bytes = null;
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(UTF_8));
            StringBuffer buffer = new StringBuffer();
            for (byte b : bytes) {
                buffer.append(String.format("%02x", b));
            }

            return buffer.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "null";
        }
    }

    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", data='" + data + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                '}';
    }

}
