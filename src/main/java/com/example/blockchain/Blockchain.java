package com.example.blockchain;
// import org.junit.Assert;
// import org.junit.Test;

import java.util.ArrayList;
// import java.util.Date;
import java.util.List;

public class Blockchain {
    List<Block> blockchain = new ArrayList<>();
    int prefix = 6;
    String prefixString = new String(new char[prefix]).replace('\0', '0');

    // @Test
    // public void givenBlockchain_whenNewBlockAdded_thenSuccess() {
    //     Block newBlock = new Block(
    //             "The is a New Block.",
    //             "b569ed5851d6cf045d148feaf6d3b01a",
    //             new Date().getTime());
    //     newBlock.mineBlock(prefix);
    //     Assert.assertTrue(newBlock.getHash().substring(0, prefix).equals(prefixString));
    //     blockchain.add(newBlock);

    //     System.out.println("Block created and entered into Blockchain");
    //     System.out.println(blockchain.get(blockchain.size()-1));
    // }
}
