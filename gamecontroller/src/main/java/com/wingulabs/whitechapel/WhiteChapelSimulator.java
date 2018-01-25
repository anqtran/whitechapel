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
        GameController gs = new GameController();
        gs.run();

    }
}
