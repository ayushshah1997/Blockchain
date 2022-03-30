package com.example.blockchain;

import java.util.ArrayList;
import java.util.List;

public class Runner {

    private static MiningNode[] miningNodes;


    // Import initial static blockchain
    // Create Nodes
    // Create graph (Adjaicency Matrix)
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
        run();
    }


    // Generate transaction at fixed interval
    // Pass transaction via gossip to nodes
    // Multithread and nodes process transaction
    // Monitor growth in blockchain size
    public static void run() {

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
