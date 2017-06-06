package com.wingulabs.whitechapel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.graph.SimpleGraph;

import com.wingulabs.whitechapel.DetectiveMove.AttemptArrest;
import com.wingulabs.whitechapel.DetectiveMove.SearchClues;
import com.wingulabs.whitechapel.DetectiveMoveResult.AttemptArrestResult;
import com.wingulabs.whitechapel.DetectiveMoveResult.SearchCluesResult;

public class MyDetectives extends Detectives {
	/**
	 *  the board game.
	 */
	private GameBoard gb = GameBoard.SINGLETON;
	//boolean array to store the rush has been used by detective.
	private boolean[] rushUsed;
	
	public MyDetectives() {
		super();
		rushUsed = new boolean[this.getDetectives().length];
	}
	/**
	 * process the detectives move.
	 */
	public void move(Jack jack) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Detectives' turn: ");
		Detective[] dts = this.getDetectives();
		// for each detective
		for (int i = 0; i < dts.length; i++) {
			String detectiveLocation = dts[i].getLocation();
			// Set contains option that detective can move.
			Set<String> moveChoices = new HashSet<String>();
			System.out.println(dts[i].getColor() + " detective's move:");
			System.out.println("Select one from the options below: ");
			System.out.println("\t 1- Stay or move 1 or 2 squares. ");
			System.out.println("\t 2- Rush (Move 3 squares without investigation). ");
			int choice = sc.nextInt();
			Set<String> adjacentLocation = getAdjacentVertex(detectiveLocation, 1);
			moveChoices.addAll(adjacentLocation);
			// Makes 2 squares move.
			for (String location : adjacentLocation) {
				moveChoices.addAll(getAdjacentVertex(location, 1));
			}
			// Rush
			if (choice == 2) {
				rushUsed[i] = true;
				//get first level
				Set<String> firstSquareSet = getAdjacentVertex(detectiveLocation, 1);
				Set<String> vertexInThreeMoves = new HashSet<String>();
				for (String firstSquare : firstSquareSet) {
					// get second levels
					Set<String> secondSquareSet = getAdjacentVertex(firstSquare, 1);
					 // add all the first and second vertex to the set.
					vertexInThreeMoves.addAll(secondSquareSet);
					for (String secondSquare : secondSquareSet) {
						Set<String> thirdSquareSet = getAdjacentVertex(secondSquare, 1);
						 // add all the second and third vertex.
						vertexInThreeMoves.addAll(thirdSquareSet);
					}
				}
				// keep the third level and remove all other vertex from the set.
				// this is rush option of jack.
				vertexInThreeMoves.removeAll(moveChoices);
				moveChoices = vertexInThreeMoves;
			}
			System.out.println("Choose one location from the list below: ");
			for (String location : moveChoices) {
				System.out.print(location + "  ");
			}
			System.out.println();
			String updatedLocation = sc.next();
			// update the past move of the detective.
			//set the detective location.
			dts[i].setLocation(updatedLocation);
			System.out.println();
		}
	}
	/**
	 * Investigation after detectives have been moved.
	 * @param detectives detectives
	 * @param jack jack
	 * @param rushUsed array contains the rush used of each detectives.
	 * @return true if jack is caught.
	 */
	public boolean investigation(
			final Jack jack, final MoveTree mt) {
		boolean caughtJack = false;
		System.out.println("Investigation Starting......");
		Scanner sc = new Scanner(System.in);
		SimpleGraph<String, Edge> mixedGraph = gb.getMixedGraph();
		Detective[] detectivesList = this.getDetectives();
		// get each detectives.
		for (int i = 0; i < detectivesList.length; i++) {
			Detective d = detectivesList[i];
			System.out.println(d.getColor() + " detective's turn:");
			 // if the detective does not use rush, start investigate.
			if (!rushUsed[i]) {
				Set<String> adjacentCircles = getAdjacentVertex(d.getLocation(), 2);
				System.out.println("Enter s if you want"
						+ " to search for clues or a to arrest:");
				String s = sc.next();
				if (s.equals("s")) {
					System.out.println("You can search clues at: ");
					for (String circle: adjacentCircles) {
						System.out.print(circle + "  ");
					}
					System.out.println();
					System.out.println("Please enter your"
							+ " searching order from the list above ( q to end): ");
					List<String> searchCluesOrder = new LinkedList<String>();
					while (sc.hasNext()) {
						String c = sc.next();
						if (c.equals("q")) {
							break;
						}
						searchCluesOrder.add(sc.next());
					}
					SearchClues clues = new SearchClues(searchCluesOrder);
					Map<String, Answer> answerMap = new HashMap<String, Answer>();
					boolean jackAnswerYes = false;
					for (String circle : searchCluesOrder) {
						Answer answer = Answer.NO;
						if (jackAnswerYes) {
							answer = Answer.NONE;
						}
						if (jack.getPastMove().contains(circle)) {
							answer = Answer.YES;
						}
						answerMap.put(circle, answer);
					}
					SearchCluesResult scr = new SearchCluesResult(clues, answerMap);
					mt.processDetectiveMoveResult(scr);

				} else if (s.equals("a")) {
					System.out.println("Choose one of the following"
							+ " circles to attempt arrest: ");
					for (String circle : adjacentCircles) {
						System.out.print(circle + "  ");
					}
					System.out.println();
					String location = sc.next();
					if (location.equals(jack.getCurrentLocation())) {
						caughtJack = true;
					}
					AttemptArrest aa = new AttemptArrest(location);
					AttemptArrestResult aar = new AttemptArrestResult(aa, caughtJack);
					mt.processDetectiveMoveResult(aar);
				}
			} else {
				System.out.println("You cannot investigate since you have used rush.");
			}
		}
		return caughtJack;
	}
	/**
	 * Get the circles that adjacent to the location.
	 * @param root current location.
	 * @param a map choice depends on the location type
	 * 1 to get the square graph.
	 * 2 to get the mixed graph.
	 * @return the set of adjacent vertex.
	 */
	public final Set<String> getAdjacentVertex(final String root, final int a) {
		SimpleGraph<String, Edge> myGraph;
		 if (a == 1) {
			myGraph = gb.getSquareGraph();
		} else {
			myGraph = gb.getMixedGraph();
		}
		Set<String> vertexSet = new HashSet<String>();
		Set<Edge> edgeSet = myGraph.edgesOf(root);
		for (Edge s : edgeSet) {
			vertexSet.add(s.getConnectedVertex(root));
		}
		return vertexSet;
	}
	public GameBoard getGb() {
		return gb;
	}
	public void setGb(GameBoard gb) {
		this.gb = gb;
	}
	public boolean[] getRushUsed() {
		return rushUsed;
	}
	public void setRushUsed(boolean[] rushUsed) {
		this.rushUsed = rushUsed;
	}
	
}