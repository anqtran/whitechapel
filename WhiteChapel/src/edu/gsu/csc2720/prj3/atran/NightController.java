package edu.gsu.csc2720.prj3.atran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eareddick.whitechapel.Answer;
import org.eareddick.whitechapel.Detective;
import org.eareddick.whitechapel.Detective.DetectiveColor;
import org.eareddick.whitechapel.DetectiveMove.AttemptArrest;
import org.eareddick.whitechapel.DetectiveMove.SearchClues;
import org.eareddick.whitechapel.DetectiveMoveResult.AttemptArrestResult;
import org.eareddick.whitechapel.DetectiveMoveResult.SearchCluesResult;
import org.eareddick.whitechapel.Detectives;
import org.eareddick.whitechapel.Edge;
import org.eareddick.whitechapel.GameBoard;
import org.eareddick.whitechapel.MoveTree;
import org.jgrapht.graph.SimpleGraph;
/**
 * Class to control all activities in the night.
 * @author anqtr
 *
 */
public class NightController {
	/**
	 * current Night.
	 */
	private Night night;
	/**
	 * Jack.
	 */
	private Jack jack;
	/**
	 * detectives.
	 */
	private Detectives detectives;
	/**
	 * return true if the night is ended.
	 * when Jack reaches the hide out, Jack is caught or
	 * number of turn greater than 15 and Jack has not got to the hideout.
	 */
	private boolean endNight = false;
	/**
	 *  the board game.
	 */
	private GameBoard gb = GameBoard.SINGLETON;
	/**
	 * past Move of all detectives.
	 */
	private ArrayList<List<String>> detectivesMove;
	/**
	 * The move tree to process Jack move.
	 */
	private MoveTree mt;
	/**
	 * Jack Hide out.
	 */
	private String hideOut;
	/**
	 * Night Controller constructor.
	 * @param night current Night.
	 * @param jack Jack.
	 * @param detectives detectives.
	 * @param hideOut Jack's hide out.
	 */
	public NightController(final Night night, final Jack jack,
			final Detectives detectives, final String hideOut) {
		super();
		this.night = night;
		this.jack = jack;
		this.detectives = detectives;
		detectivesMove = new ArrayList<List<String>>();
		mt = new MyMoveTree(GameBoard.SINGLETON, jack.getCurrentLocation());
		this.hideOut = hideOut;
	}
	/**
	 * Start the night.
	 * @return true if jack sucessfully reaches the hideout.
	 */
	public final boolean run() {
		for (int i = 0; i < 5; i++) {
			detectivesMove.add(new ArrayList<String>());
			detectivesMove.get(i).add(detectives.getDetectives()[i].getLocation());
		}
		System.out.println("Night " + night.getCurrentNight() + " begins!!! ");
		while (!endNight) {
			int turn = night.getCurrentTurn() + 1;
			System.out.println("Turn " + turn);
			jackMove(detectives);
			detectivesMove();
			night.setCurrentTurn(turn);
			// if jack reaches the hideouts, start new night.
			if (jack.getCurrentLocation().equals(hideOut)) {
				System.out.println("Jack got to his hide out, night " +
			night.getCurrentNight() + 1 + " will start soon...");
				endNight = true;
			} else {
				if (turn == 15) {
					System.out.println("Jack cannot reach his hideOut in the night"
							+ ". Game Over!");
					return false;
				}
			}
			System.out.println("Turn " + turn + " ends. ");
		}
		return true;
	}
	/**
	 * process jack moves.
	 * @param detectives detectives.
	 */
	private void jackMove(final Detectives detectives) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Jack's turn: ");
		System.out.println("Choose one location from the list below: ");
		Set<String> adjacentLocation =
				getAdjacentVertex(jack.getCurrentLocation(), 0);
		for (String location : adjacentLocation) {
			System.out.print(location + "  ");
		}
		System.out.println();
		String updatedLocation = sc.next();
		jack.setCurrentLocation(updatedLocation);
		// mt.processJackMove(detectives); Need to complete the tree update.
		System.out.println();

	}
	/**
	 * process the detectives move.
	 */
	private void detectivesMove() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Detectives' turn: ");
		Detective[] dts = detectives.getDetectives();
		//boolean array to store the rush has been used by detective.
		boolean[] rushUsed = new boolean[dts.length];
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
			detectivesMove.get(i).add(updatedLocation);
			//set the detective location.
			dts[i].setLocation(updatedLocation);
			System.out.println();
		}
		investigation(detectives, jack, rushUsed);
	}
	/**
	 * Investigation after detectives have been moved.
	 * @param detectives detectives
	 * @param jack jack
	 * @param rushUsed array contains the rush used of each detectives.
	 */
	private void investigation(final Detectives detectives,
			final Jack jack, final boolean[] rushUsed) {
		System.out.println("Investigation Starting......");
		Scanner sc = new Scanner(System.in);
		SimpleGraph<String, Edge> mixedGraph = gb.getMixedGraph();
		Detective[] detectivesList = detectives.getDetectives();
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
					SearchClues clues = new SearchClues(detectivesMove.get(i),
							searchCluesOrder);
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
					boolean answer = false;
					if (location.equals(jack.getCurrentLocation())) {
						answer = true;
						endNight = true;
					}
					AttemptArrest aa = new AttemptArrest(detectivesMove.get(i), "C82");
					AttemptArrestResult aar = new AttemptArrestResult(aa, answer);
					mt.processDetectiveMoveResult(aar);
				}
			} else {
				System.out.println("You cannot investigate since you have used rush.");
			}
		}
	}
	/**
	 * Get the circles that adjacent to the location.
	 * @param root current location.
	 * @param a map choice depends on the location type
	 * 0 to get the circle graph.
	 * 1 to get the square graph.
	 * 2 to get the mixed graph.
	 * @return the set of adjacent vertex.
	 */
	public final Set<String> getAdjacentVertex(final String root, final int a) {
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
		for (Edge s : edgeSet) {
			vertexSet.add(s.getConnectedVertex(root));
		}
		return vertexSet;
	}
}

