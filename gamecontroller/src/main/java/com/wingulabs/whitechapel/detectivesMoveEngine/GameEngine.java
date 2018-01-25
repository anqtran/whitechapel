package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.io.IOException;
import java.util.List;

import com.wingulabs.whitechapel.Utility.DetectivesEngineUtility;
import com.wingulabs.whitechapel.detectives.Detectives;

/**
 * Contains methods that could be included in a program that provides artificial
 * intelligence decisions for the detectives.
 *
 * @author Anwar Reddick
 *
 */
public interface GameEngine {

    /**
     * Given a MoveTree and the locations of detectives, determines the circle
     * vertex that should be prioritized for questioning.
     *
     * @param loc
     *            detective locations.
     * @param moveTree
     *            the potential paths that Jack has taken.
     * @return the circle vertex that should be prioritized for questioning.
     */
    String getPriorityVertex(Detectives loc, MoveTree moveTree);

    /**
     * Determines the move that the specified detective should make, given the
     * MoveTree and the locations of all the detectives.
     *
     * @param loc detective locations. @param moveTree the potential paths that
     * Jack has taken. @param detectiveIndex the detective for whom to determine
     * a move. @return The move the detective should make. @throws
     */
    String getMove(String priorityVertex, Detectives loc, DetectivesEngineUtility dUtility, int detectiveIndex)
            throws IOException;

    /**
     * Get all of the moves of detectives in one turn. return a list of
     * Detective Moves.
     * @throws IOException throws when cannot find file.
     */
    List<String> setDetectiveMoves(Detectives dts, MoveTree mt) throws IOException;
}
