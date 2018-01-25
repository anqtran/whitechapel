package com.wingulabs.whitechapel.nightController;

import java.util.List;

import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;

public class DetectiveEngineController extends AbstractDetectiveController {

    private String root;
    private Detectives dt;

    public DetectiveEngineController(GameBoard gb, Detectives dt, String jackStartingLocation) {
        super(gb);
        root = jackStartingLocation;
        this.dt = dt;
    }

    @Override
    List<String> detectivesMove() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    boolean investigation(Jack jack) {
        // TODO Auto-generated method stub
        return false;
    }

}
