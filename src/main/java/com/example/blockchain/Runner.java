package com.example.blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Runner implements Runnable{

    private static MiningNode[] miningNodes;

    /*
    * Runs:
    * 1. Without Sharding Block Capacity - 256 txns
    * 2. Without Sharding Block Capacity - 2048 txns
    * 3. With Sharding Block Capacity - 256 txns
    * 4. Without Sharding Block Capacity - 2048 txns
    * */



    // Import initial static blockchain
    // Create Nodes - Done
    // Create graph (Adjaicency Matrix) - Done
    // Create Users
    public static void setup() {

        miningNodes = new MiningNode[100];
        int[][] graphAdjaicencyMatrix = new int[100][100];

        for(int i =0; i < 100; i++){
            for(int j=i*10; j<10; j++){
                if(j!=i){
                    graphAdjaicencyMatrix[i][j] = 1;
                }
            }
        }

        for(int i =0; i < 100; i++){
            List<MiningNode> temp = new ArrayList<>();
            for(int j = 0; j < 100; j++){
                if(graphAdjaicencyMatrix[i][j] == 1) {
                    temp.add(miningNodes[j]);
                }
            }
            miningNodes[i].setNeigbours(temp);
        }
    }

    public static void main(String[] args) {
        setup();
        runMining();
    }


    // Generate transaction at fixed interval
    // Pass transaction via gossip to nodes
    // Multithread and nodes process transaction
    // Monitor growth in blockchain size
    public static void runMining() {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                // Generate Trxn
                for(int i=0; i<100; i++){
                    Transaction temp = new Transaction();
                    miningNodes[i].listeningPort(temp);

                }
            }
        },0,3000);
    }

    @Override
    public void run() {

    }


    /*
    * Node Class
    * Unique Address
    * Find nonce
    *
    * */

    /*
    * Sharded Run
    *
    * Divide into sub network
    *
    * */



}
