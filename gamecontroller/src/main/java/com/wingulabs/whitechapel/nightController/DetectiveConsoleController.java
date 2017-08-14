package com.wingulabs.whitechapel.nightController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.wingulabs.whitechapel.Utility.GraphUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;

public class DetectiveConsoleController extends AbstractDetectiveController {

	/**
	 * an array contains whether a detective at an index uses rush or not.
	 */
	private boolean[] rushUsed;

	private Detectives detectives;

	public DetectiveConsoleController(final GameBoard gb, final Detectives detectives) {
		super(gb);
		this.detectives = detectives;
	}

	private String selectDetectivesTargetedLocation(final Set<String> possibleLocation, final int detectiveIndex,
			final List finalDestination) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose one location from the list below: ");
		for (String location : possibleLocation) {
			System.out.print(location + "  ");
		}
		System.out.println();
		String updatedLocation = sc.next();
		boolean checkDuplicate = checkDuplicatePosition(updatedLocation, detectiveIndex, finalDestination);
		while (!possibleLocation.contains(updatedLocation) || checkDuplicate) {
			System.out.println("Your choice is not in the set or there is already a detective" + " at that location."
					+ " Please select another one: ");
			updatedLocation = sc.next();
			checkDuplicate = checkDuplicatePosition(updatedLocation, detectiveIndex, finalDestination);
		}
		return updatedLocation;
	}

	@Override
	protected final List<String> detectivesMove() {

		rushUsed = new boolean[detectives.getDetectives().length];
		List finalDestination = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		System.out.println("Detectives' turn: ");
		Detective[] dts = detectives.getDetectives();
		// for each detective
		for (int i = 0; i < dts.length; i++) {
			String detectiveLocation = dts[i].getLocation();
			// Set contains option that detective can move.
			Set<String> moveChoices = new HashSet<String>();
			Detective currentDetective = dts[i];
			System.out.println(currentDetective.getColor() + " detective's move:");
			System.out.println("The detective current location: " + currentDetective.getLocation());
			System.out.println("Select one from the options below: ");
			System.out.println("\t 1- Stay or move 1 or 2 squares. ");
			System.out.println("\t 2- Rush (Move 3 squares without investigation). ");
			int choice = sc.nextInt();
			Set<String> adjacentLocation = GraphUtility.getAdjacentSquare(detectiveLocation, gb);
			moveChoices.addAll(adjacentLocation);
			// Makes 2 squares move.
			for (String location : adjacentLocation) {
				moveChoices.addAll(GraphUtility.getAdjacentSquare(location, gb));
			}
			// Rush
			if (choice == 2) {
				rushUsed[i] = true;
				// get first level
				Set<String> firstSquareSet = GraphUtility.getAdjacentSquare(detectiveLocation, gb);
				Set<String> vertexInThreeMoves = new HashSet<String>();
				for (String firstSquare : firstSquareSet) {
					// get second levels
					Set<String> secondSquareSet = GraphUtility.getAdjacentSquare(firstSquare, gb);
					// add all the first and second vertex to the set.
					vertexInThreeMoves.addAll(secondSquareSet);
					for (String secondSquare : secondSquareSet) {
						Set<String> thirdSquareSet = GraphUtility.getAdjacentSquare(secondSquare, gb);
						// add all the second and third vertex.
						vertexInThreeMoves.addAll(thirdSquareSet);
					}
				}
				// keep the third level and remove all other vertex from the
				// set.
				// this is rush option of jack.
				vertexInThreeMoves.removeAll(moveChoices);
				moveChoices = vertexInThreeMoves;
			}

			String updatedLocation = selectDetectivesTargetedLocation(moveChoices, i, finalDestination);
			// update the past move of the detective.
			// set the detective location.
			finalDestination.add(updatedLocation);
			System.out.println();
		}
		return finalDestination;
	}

	@Override
	final boolean investigation(final Jack jack) {

		boolean caughtJack = false;
		System.out.println("Investigation Starting......");
		Scanner sc = new Scanner(System.in);
		Detective[] detectivesList = detectives.getDetectives();
		// get each detectives.
		for (int i = 0; i < detectivesList.length; i++) {
			Detective d = detectivesList[i];
			System.out.println(d.getColor() + " detective's turn:");
			// if the detective does not use rush, start investigate.
			if (!rushUsed[i]) {
				Set<String> adjacentCircles = GraphUtility.getAdjacentCircle(d.getLocation(), gb);
				System.out.println("Enter s if you want" + " to search for clues or a to arrest:");
				String s = sc.next();
				if (s.equals("s")) {
					System.out.println("You can search clues at: ");
					for (String circle : adjacentCircles) {
						System.out.print(circle + "  ");
					}
					System.out.println();
					System.out.println("Please enter your" + " searching order from the list above ( q to end): ");
					List<String> searchCluesOrder = new LinkedList<String>();
					while (sc.hasNext()) {
						String c = sc.next();
						if (c.equals("q")) {
							break;
						}
						searchCluesOrder.add(c);
					}
					boolean jackAnswerYes = false;
					for (String circle : searchCluesOrder) {
						if (jackAnswerYes) {
							break;
						}
						if (jack.getPastMove().contains(circle)) {
							jackAnswerYes = true;
							System.out.println("Jack has been to " + circle);
						} else {
							System.out.println("Jack has not been to " + circle);
						}
					}
				} else if (s.equals("a")) {
					System.out.println("Choose one of the following" + " circles to attempt arrest: ");
					for (String circle : adjacentCircles) {
						System.out.print(circle + "  ");
					}
					System.out.println();
					String location = sc.next();
					if (location.equals(jack.getCurrentLocation())) {
						caughtJack = true;
						System.out.println("You caught Jack! Mission accomplished. ");
						return true;
					} else {
						System.out.println("Jack is not here! ");
					}
				}
			} else {
				System.out.println("You cannot investigate since you have used rush.");
			}
		}
		return caughtJack;
	}

	private boolean checkDuplicatePosition(String destination, int detective, List finalDestination) {
		if (finalDestination.contains(destination)) {
			return true;
		}
		for (int i = detective + 1; i < detectives.getDetectives().length; i++) {
			String detectiveLocation = detectives.getDetectives()[i].getLocation();
			if (detectiveLocation == destination) {
				return true;
			}
		}
		return false;
	}

}
