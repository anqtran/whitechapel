package com.wingulabs.whitechapel.nightController;

import java.util.Set;

import org.jgrapht.graph.SimpleGraph;

import com.wingulabs.whitechapel.Utility.ConsoleUtility;
import com.wingulabs.whitechapel.Utility.GraphUtility;
import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;
import com.wingulabs.whitechapel.night.Night;

public class JackConsoleController extends AbstractJackController {

	public JackConsoleController(final GameBoard gb, final Jack jack, Night night) {
		super(gb,jack,night);
	}

	/**
	 * Normal move of Jack.
	 * @param source Source.
	 * @return destination.
	 */
	private String regularMove(final String source) {
		Set<String> adjacentLocation = getJackAdjacentVertex(source);
		String updatedLocation = ConsoleUtility.selectLocationfromSet(adjacentLocation);
		return updatedLocation;
	}
	
	/**
	 * Coach move of Jack.
	 * @param source source.
	 * @return destination.
	 */
	private String coachMove(final String source) {
		Set<String> adjacentLocation =
				getJackAdjacentVertex(source);
		System.out.println("First move of Coach:");
		String firstMoveLocation = ConsoleUtility.selectLocationfromSet(adjacentLocation);
		jack.setCurrentLocation(firstMoveLocation);
		adjacentLocation = getJackAdjacentVertex(firstMoveLocation);
		//second move of coach
		System.out.println("Second move of Coach:");
		String secondMoveLocation = ConsoleUtility.selectLocationfromSet(adjacentLocation);
		return secondMoveLocation;
	}
	
	/**
	 * alley move of jack.
	 * @param source source.
	 * @return destination.
	 */
	private String alleyMove(final String source) {
		SimpleGraph<String, Edge> alleyGraph = gb.getAlleyCircleGraph();
		Set<String> alleyMoveSet = GraphUtility.getAdjacentVertex(source, alleyGraph);
		String updatedLocation = ConsoleUtility.selectLocationfromSet(alleyMoveSet);
		return updatedLocation;
	}

	@Override
	protected final String jackMove() {
		
		System.out.println("Jack's turn: ");
		System.out.println("Jack currently has "
		+ jack.getCoachAlley()[0] + " coach tokens and "
				+ jack.getCoachAlley()[1] + " alley tokens.");
		System.out.println("Jack current location: " + jack.getCurrentLocation());
		System.out.println("Select one from the options below: ");
		System.out.println("\t 1- To make a normal move. ");
		System.out.println("\t 2- To use coach special token. ");
		System.out.println("\t 3- To use alley special token. ");
		int choice = ConsoleUtility.getIndexSelection(1, 3);

		boolean check = false;
		while (!check) {
			if (choice == 2 && jack.getCoachAlley()[0] == 0) {
				System.out.println("Jack has used all of the coach token "
			+ " please select another move.");
				choice = ConsoleUtility.getIndexSelection(1, 3);
			} else if (choice == 3 && jack.getCoachAlley()[1] == 0) {
				System.out.println("Jack has used all of the alley token "
			+ " please select another move.");
				choice = ConsoleUtility.getIndexSelection(1, 3);
			} else if ( choice > 3 && choice < 1) {
				
			} else {
				check = true;
			}
		}
		switch (choice) {
			case 1:
				return regularMove(jack.getCurrentLocation());
			case 2:
				jack.getCoachAlley()[0]--;
				night.setCurrentTurn(night.getCurrentTurn() + 1);
				return coachMove(jack.getCurrentLocation());
			case 3:
				jack.getCoachAlley()[1]--;
				return alleyMove(jack.getCurrentLocation());
			default:
				return null;
		}
	}
}
