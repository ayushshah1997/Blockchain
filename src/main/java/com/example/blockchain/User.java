package com.example.blockchain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class User {

  private PublicKey pubKey;
  private PrivateKey pvtKey;
  private double balance;

  public User(double balance) {
    KeyPair kp;
    try {
      kp = generateRSAKeyPair();
      this.pubKey = kp.getPublic();
      this.pvtKey = kp.getPrivate();
      this.balance = balance;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PublicKey getPubKey() {
    return this.pubKey;
  }

  public void setPubKey(PublicKey pubKey) {
    this.pubKey = pubKey;
  }

  public PrivateKey getPvtKey() {
    return this.pvtKey;
  }

  public double getBalance() {
    return this.balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public static KeyPair generateRSAKeyPair() throws Exception {
    SecureRandom sr = new SecureRandom();
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(2048, sr);
    return kpg.generateKeyPair();
  }
}
