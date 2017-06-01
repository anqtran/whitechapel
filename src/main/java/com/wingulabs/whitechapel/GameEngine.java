package com.wingulabs.whitechapel;

/**
 * Contains methods that could be included in a program that provides
 * artificial intelligence decisions for the detectives.
 *
 * @author Anwar Reddick
 *
 */
public interface GameEngine {

	/**
	 * Given a MoveTree and the locations of detectives, determines the circle
	 * vertex that should be prioritized for questioning.
	 *
	 * @param loc detective locations.
	 * @param moveTree the potential paths that Jack has taken.
	 * @return the circle vertex that should be prioritized for questioning.
	 */
	String getPriorityVertex(Detectives loc, MoveTree moveTree);

	/**
	 * Determines the move that the specified detective should make, given the
	 * MoveTree and the locations of all the detectives.
	 *
	 * @param loc detective locations.
	 * @param moveTree the potential paths that Jack has taken.
	 * @param detectiveIndex the detective for whom to determine a move.
	 * @return The move the detective should make.
	 */
	DetectiveMove getMove(Detectives loc, MoveTree moveTree,
			int detectiveIndex);

}
