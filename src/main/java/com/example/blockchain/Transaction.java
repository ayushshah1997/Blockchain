package com.example.blockchain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.Timestamp;
import java.util.Scanner;
//import javax.xml.bind.DatatypeConverter;

public class Transaction {

    private static String senderAddress; // Hash of public key
    private static String payeeAddress;
    private static double amount;
    private static Timestamp timestamp;
    private static Signature sign;
    private static PrivateKey senderPvtKey;
    private static PublicKey senderPubKey;

    public Transaction(User sender, String payee, Timestamp time, double amt) {
        senderPvtKey = sender.getPvtKey();
        senderPubKey = sender.getPubKey();
        timestamp = time;
        amount = amt;
    }

    private static final String SIGNING_ALGORITHM = "SHA256withRSA";
    private static final String RSA = "RSA";

    public Transaction() {

    }

    //driver code
    public static void main(String args[]) throws Exception {
        String input = "Java is an" + "object-oriented language";

        byte[] sig = createDigitalSignature(input.getBytes(), senderPvtKey);
        //System.out.println("Signature Value:\n " + DatatypeConverter.printHexBinary(sig));
        System.out.println("Verification: "+ verifyDigitalSignature(input.getBytes(), sig, senderPubKey));
    }

    public static byte[] createDigitalSignature(byte[] input, PrivateKey Key) throws Exception {
        Signature sig = Signature.getInstance(SIGNING_ALGORITHM);
        sig.initSign(Key);
        sig.update(input);
        return sig.sign();
    }

    //function verifies the signature by using the public key
    public static boolean verifyDigitalSignature(byte[] input, byte[] signatureToVerify, PublicKey key) throws Exception {
        Signature sig = Signature.getInstance(SIGNING_ALGORITHM);
        sig.initVerify(key);
        sig.update(input);
        return sig.verify(signatureToVerify);
    }

}