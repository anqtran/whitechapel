package com.wingulabs.whitechapel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wingulabs.whitechapel.Utility.DetectivesEngineUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectivesMoveEngine.MyMoveTree;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.gameController.GameController;

/**
 * Class starts the game.
 * 
 * @author anqtr
 *
 */
public final class WhiteChapelSimulator {
    /**
     * Private constructor prevents extension.
     */
    private WhiteChapelSimulator() {
    }

    /**
     * Main class to start the game.
     * 
     * @param args
     * 
     * @throws IOException
     * 
     */
    public static void main(final String[] args) throws IOException {
        GameBoard gb = GameBoard.SINGLETON;
        MyMoveTree mt = new MyMoveTree(gb, "C65");
        DetectivesEngineUtility dUtility = new DetectivesEngineUtility(mt, new boolean[5]);
        System.out.println(dUtility.getDetectivePath("SC40S1", "C65"));
        System.out.println(dUtility.getDistance("SC40S1", "SC51S1W1"));
        // System.out.println(dUtility.getDetectivePath("SC40S1",
        // "C66").toString());

        // dUtility.getDetectivePath("C62", "C22");

        // mt.processJackMove(dt);
        //// mt.processDetectiveMoveResultTester("C66", Answer.NO);
        // mt.processJackMove(dt);
        // mt.processJackMove(dt);
        // mt.processJackMove(dt);
        // mt.processJackMove(dt);
        // MyGameEngine engine = new MyGameEngine(mt,dt);
        //
        // engine.getPriorityVertex(dt, mt);
        // mt.processDetectiveMoveResultTester("C66", Answer.NO);

        GameController gs = new GameController();
        gs.run();

    }
}
