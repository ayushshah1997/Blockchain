package com.example.blockchain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class User {

  public static void main(String[] args) {
    System.out.println("Hello world!");
  }

  private PublicKey pubKey;
  private PrivateKey pvtKey;

  public User() {
    KeyPair kp;
    try {
      kp = generateRSAKeyPair();
      pubKey = kp.getPublic();
      pvtKey = kp.getPrivate();
    } catch (Exception e) {
      System.out.println("Could not generate key pair");
      e.printStackTrace();
    }
  }

  public PublicKey getPubKey() {
    return pubKey;
  }

  public void setPubKey(PublicKey pubKey) {
    this.pubKey = pubKey;
  }

  public PrivateKey getPvtKey() {
    return pvtKey;
  }

  public void setPvtKey(PrivateKey pvtKey) {
    this.pvtKey = pvtKey;
  }

  public static KeyPair generateRSAKeyPair() throws Exception {
    SecureRandom sr = new SecureRandom();
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(2048, sr);
    return kpg.generateKeyPair();
  }
}
