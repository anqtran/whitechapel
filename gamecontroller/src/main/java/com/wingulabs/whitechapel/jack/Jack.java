package com.wingulabs.whitechapel.jack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.graph.SimpleGraph;

import com.wingulabs.whitechapel.gameBoard.GameBoard;
/**
 * White Chapel Jack.
 * @author anqtr
 *
 */
public class Jack {
	/**
	 * Special tokens jack has in each night.
	 * The first index is coach, second is alley.
	 * These two numbers change in every night.
	 */
	private int[] coachAlley;
	/**
	 * List of past move of Jack not including current location.
	 * Used to the search clues of detectives.
	 */
	private List<String> pastMove;
	/**
	 * current Location of Jack.
	 */
	private String currentLocation;
	/**
	 *  the board game.
	 */
	private GameBoard gb = GameBoard.SINGLETON;
	/**
	 * Constructor of Jack's class.
	 * @param coachAlley special tokens.
	 * @param currentLocation where is jack right now.
	 */
	public Jack(final int[] coachAlley, final String currentLocation) {
		this.coachAlley = coachAlley;
		this.pastMove =  new ArrayList<String>();
		this.currentLocation = currentLocation; // starting at crime scene.
		pastMove.add(currentLocation);
	}
	/**
	 * Coach Alley Getter.
	 * @return coachalley
	 */
	public final int[] getCoachAlley() {
		return coachAlley;
	}
	/**
	 * Coach alley setter.
	 * @param coachAlley coachAlley.
	 */
	public final void setCoachAlley(final int[] coachAlley) {
		this.coachAlley = coachAlley;
	}
	/**
	 * Past move getter.
	 * @return Past move of jack.
	 */
	public final List<String> getPastMove() {
		return pastMove;
	}
	/**
	 * Get current location of Jack.
	 * @return current location.
	 */
	public final String getCurrentLocation() {
		return currentLocation;
	}
	/**
	 * Set current location to new location.
	 * Add the previous location to past move.
	 * @param currentLocation new Jack location.
	 */
	public final void setCurrentLocation(final String currentLocation) {
		pastMove.add(currentLocation);
		this.currentLocation = currentLocation;
	}

}
