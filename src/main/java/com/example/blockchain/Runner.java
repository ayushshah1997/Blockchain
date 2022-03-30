package com.example.blockchain;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class Runner implements Runnable{

    private static MiningNode[] miningNodes;

    private static User[] users;

    private static Random r = new Random();
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


        users = new User[100];

        for(int i = 0; i< users.length; i++ ){
            users[i] = new User(1000.0);
        }

        miningNodes = new MiningNode[100];
        int[][] graphAdjacencyMatrix = new int[100][100];

        for(int i =0; i < 100; i++){
            for(int j=0; j<10; j++){
                int idx = r.nextInt(100);
                if(idx!=i){
                    graphAdjacencyMatrix[i][idx] = 1;
                }
            }
        }

        for(int i =0; i < 100; i++){
            List<MiningNode> temp = new ArrayList<>();
            for(int j = 0; j < 100; j++){
                if(graphAdjacencyMatrix[i][j] == 1) {
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
                    int u1Idx = r.nextInt(100);
                    User u1 = users[u1Idx];
                    User u2 = users[( u1Idx + r.nextInt(99))%100];

                    Date date = new Date();

                    // getting the object of the Timestamp class
                    Timestamp ts = new Timestamp(date.getTime());

                    Transaction temp = new Transaction(u1, u2.getPubKey().toString(), ts, r.nextDouble());

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
