package com.wingulabs.whitechapel.nightController;

import java.util.List;

import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;

/**
 * Makes decisions for detectives.
 * 
 * @author anwar
 *
 */
public abstract class AbstractDetectiveController {

    protected final GameBoard gb;

    public AbstractDetectiveController(final GameBoard gb) {
        this.gb = gb;
    }

    /**
     * process the detectives move.
     * 
     * @return List of destination of detectives.
     */
    abstract List<String> detectivesMove();

    /**
     * Investigation at the end of detectives move.
     * 
     * @param jack
     *            jack.
     * @return true if jack was caught.
     */
    abstract boolean investigation(Jack jack);

}
