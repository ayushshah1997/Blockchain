package com.example.blockchain;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class MainClass {
    static boolean verifySignature(byte[] messageBytes, byte[] digitalSignature, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(messageBytes);
            return signature.verify(digitalSignature);
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            System.out.println("Error while verifying the signature");
            e.printStackTrace();
            return false;
        }
    }

    static byte[] getDigitalSignature(PrivateKey privateKey, byte[] messageBytes) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(messageBytes);
            return signature.sign();
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            System.out.println("Error while signing the transaction");
            e.printStackTrace();
            return null;
        }
    }

    static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error while generating the key pair");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
    }

}
