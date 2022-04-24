package com.example.blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

//import javax.xml.bind.DatatypeConverter;

public class Transaction {

    private static String senderAddress; // Hash of public key
    private static String payeeAddress;
    private static double amount;
    private static long timestamp;
    private static byte[] sign;
    private static byte[] inputMessage;
    private static PrivateKey senderPvtKey;
    private static PublicKey senderPubKey;

    public static String getSenderAddress() {
        return senderAddress;
    }

    public static void setSenderAddress(String senderAddress) {
        Transaction.senderAddress = senderAddress;
    }

    public static String getPayeeAddress() {
        return payeeAddress;
    }

    public static void setPayeeAddress(String payeeAddress) {
        Transaction.payeeAddress = payeeAddress;
    }

    public static double getAmount() {
        return amount;
    }

    public static void setAmount(double amount) {
        Transaction.amount = amount;
    }

    public static long getTimestamp() {
        return timestamp;
    }

    public static void setTimestamp(long timestamp) {
        Transaction.timestamp = timestamp;
    }

    public static byte[] getSign() {
        return sign;
    }

    public static void setSign(byte[] sign) {
        Transaction.sign = sign;
    }

    public static PrivateKey getSenderPvtKey() {
        return senderPvtKey;
    }

    public static void setSenderPvtKey(PrivateKey senderPvtKey) {
        Transaction.senderPvtKey = senderPvtKey;
    }

    public static PublicKey getSenderPubKey() {
        return senderPubKey;
    }

    public static void setSenderPubKey(PublicKey senderPubKey) {
        Transaction.senderPubKey = senderPubKey;
    }

    public static byte[] getInputMessage() {
        return inputMessage;
    }

    public static void setInputMessage(byte[] inputMessage) {
        Transaction.inputMessage = inputMessage;
    }

    public Transaction(User sender, String payee, long time, double amt, byte[] input, PrivateKey privKey) {
        senderPvtKey = sender.getPvtKey();
        senderPubKey = sender.getPubKey();
        timestamp = time;
        amount = amt;
        inputMessage = input;
        try {
            sign=createDigitalSignature(input, privKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String SIGNING_ALGORITHM = "SHA256withRSA";
    private static final String RSA = "RSA";

    public byte[] createDigitalSignature(byte[] input, PrivateKey Key) throws Exception {
        Signature sig = Signature.getInstance(SIGNING_ALGORITHM);
        sig.initSign(Key);
        sig.update(input);
        return sig.sign();
    }

    // function verifies the signature by using the public key
    public boolean verifyDigitalSignature(byte[] input, byte[] signatureToVerify, PublicKey key)
            throws Exception {
        Signature sig = Signature.getInstance(SIGNING_ALGORITHM);
        sig.initVerify(key);
        sig.update(input);
        return sig.verify(signatureToVerify);
    }

}