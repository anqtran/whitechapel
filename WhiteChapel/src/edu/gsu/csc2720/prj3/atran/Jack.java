package edu.gsu.csc2720.prj3.atran;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.eareddick.whitechapel.Detectives;
import org.eareddick.whitechapel.Edge;
import org.eareddick.whitechapel.GameBoard;
import org.jgrapht.graph.SimpleGraph;
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
		pastMove.add(this.currentLocation);
		this.currentLocation = currentLocation;
	}
	/**
	 * process jack moves.
	 * @param detectives detectives.
	 */
	public final void move(final Detectives detectives) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Jack's turn: ");
		System.out.println("Choose one location from the list below: ");
		Set<String> adjacentLocation =
				getJackAdjacentVertex(currentLocation, 0);
		for (String location : adjacentLocation) {
			System.out.print(location + "  ");
		}
		System.out.println();
		String updatedLocation = sc.next();
		currentLocation = updatedLocation;
		// mt.processJackMove(detectives); Need to complete the tree update.
		System.out.println();

	}
	public final Set<String> getJackAdjacentVertex(final String root, final int a) {
		SimpleGraph<String, Edge> myGraph;
		if (a == 0) {
			myGraph = gb.getCircleGraph();
		}
		else if (a == 1) {
			myGraph = gb.getSquareGraph();
		} else {
			myGraph = gb.getMixedGraph();
		}
		Set<String> vertexSet = new HashSet<String>();
		Set<Edge> edgeSet = myGraph.edgesOf(root);
		MySquareJackMoveTree squareMoveTree = new MySquareJackMoveTree(gb,root);
		for (Edge s : edgeSet) {
			// check if there is any detectives on the path
			String Destination = s.getConnectedVertex(root);
			if(!squareMoveTree.checkDetectivesOnPath(Destination)) {
			vertexSet.add(Destination);
			}
		}
		return vertexSet;
	}
}
