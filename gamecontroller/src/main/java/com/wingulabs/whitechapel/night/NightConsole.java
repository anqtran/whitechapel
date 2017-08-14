package com.wingulabs.whitechapel.night;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.night.Night;

/**
 * Generates the night of whitechapel from the user's input.
 *
 * @author anqtr
 *
 */
public class NightConsole extends Night {
	/**
	 * GameBoard of the game.
	 */
	private GameBoard gb = GameBoard.SINGLETON;
	/**
	 * all the crime scenes on the board.
	 */
	protected Set<String> crimeSceneSet;
	/**
	 * All the starting point of the detectives.
	 */
	protected Set<String> detectivesStartingLocation;
	/**
	 * Night Console Constructor.
	 *
	 * @param currentNight number of night.
	 * @param detectivesStartingLocation starting locations of detectives.
	 * @param crimeSceneSet starting locations of Jack.
	 */
	public NightConsole(final int currentNight, final Set<String>
	detectivesStartingLocation, final Set<String> crimeSceneSet) {
		super(currentNight);
		this.crimeSceneSet = new HashSet<String>(crimeSceneSet);
		this.markedToken = markedTokenGenerator();
		this.fakeToken = fakeTokenGenerator();
		this.detectivesStartingLocation =
				detectivesStartingLocationGenerator(currentNight,detectivesStartingLocation);
		this.detectives = detectivesGenerator();
		this.fakePatrolToken = fakePatrolTokenGenerator();
		this.crimeScene = crimeSceneGenerator();
	}
	/**
	 * Generates the set of starting locations of detectives from the given set.
	 * @param detectivesStartingLocation set of starting locations
	 * @return the set of detectives' locations in the night.
	 */
	protected final Set<String> detectivesStartingLocationGenerator( int nightNo,
			Set<String> detectivesStartingLocation) {
		if(nightNo == 1 ) {
			return new HashSet<>(detectivesStartingLocation);
		} else {
			Set<String> startingLocation =
					new HashSet<String>(detectivesStartingLocation);
			GameBoard gb = GameBoard.SINGLETON;
			Set<String> yellowSquare = new HashSet<String>(gb.YELLOW_SQUARES);
			yellowSquare.removeAll(startingLocation);
			System.out.println("Detectives start at 5 location at the end "
					+ "of the previous night.");
			for (int i = 0; i < 2; i++) {
				System.out.println("Select two more tokens"
						+ " from the yellow starting square: ");
				String token = selectTargetedLocation(yellowSquare);
				startingLocation.add(token);
				yellowSquare.remove(token);
			}
			if(nightNo == 3) {
				String fakeCrimeScene = fakeCrimeSceneGenerator();
				super.setFakeCrimeScene(fakeCrimeScene);
			}
			return startingLocation;
		}
	}
	

	/**
	 * Ask Jack to place the marked token to set up the night.
	 *
	 * @return the locations of all the marked token.
	 */
	protected final List<String> markedTokenGenerator() {
		System.out.println("Setting up Crime Scene! ");
		List<String> markedToken = new ArrayList<>();
		int numberOfMarkedToken = womenToken[0];
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < numberOfMarkedToken; i++) {
			System.out.println("Jack is choosing marked token: ");
			String choseToken = selectTargetedLocation(crimeSceneSet);
			markedToken.add(choseToken);
			crimeSceneSet.remove(choseToken);
		}
		startingMarkedToken = new ArrayList<String>(markedToken);
		return markedToken;
	}

	/**
	 * Ask Jack to place the fake token to set up the night.
	 *
	 * @return the locations of fake token.
	 */
	protected final Set<String> fakeTokenGenerator() {
		Scanner sc = new Scanner(System.in);
		Set<String> fakeToken = new HashSet<String>();
		int numberOfFakeToken = womenToken[1];
		for (int i = 0; i < numberOfFakeToken; i++) {
			System.out.println("Jack is choosing fake token: ");
			String choseToken = selectTargetedLocation(crimeSceneSet);
			fakeToken.add(choseToken);
			crimeSceneSet.remove(choseToken);
		}
		return fakeToken;
	}

	/**
	 * Place the detectives in different location at the beginning of the night.
	 *
	 * @return detectives.
	 */
	protected final Detectives detectivesGenerator(){
		Scanner sc = new Scanner(System.in);
		Detectives dt = new Detectives();
		Detective[] dts = dt.getDetectives();
		for (int i = 0; i < dts.length; i++) {
			System.out.println(dts[i].getColor() + " detective:");
			String choseLocation = selectTargetedLocation(detectivesStartingLocation);
			dts[i].setLocation(choseLocation);
			realPatrolToken.add(choseLocation);
			detectivesStartingLocation.remove(choseLocation);
		}
		return dt;
	}

	/**
	 * Generate the crime scene for each night. From the locations of all the
	 * tokens and detectives, ask Jack to wait or kill. If wait, let the
	 * detectives move all the marked token.
	 *
	 * @return the crime scene of the night.
	 */
	protected final String crimeSceneGenerator() {
		String crimeScence = "";
		Scanner scan = new Scanner(System.in);
		boolean kill = false;
		List<String> revealedPatrolToken = new ArrayList<String>();
		String input = "";
		while (!kill) {
			if (currentTurn > -5) {
				System.out.println("Jack, do you want to kill or wait? ");
				System.out.println("If you want to kill,"
				+ " enter k or press any button to wait. ");
				input = scan.next();
			} else {
				System.out.println("Jack has waited for 5 turns."
						+ " He has to kill the victim. ");
			}
			// if Jack choose to kill.
			if (input.equals("k") || currentTurn == -5) {
				kill = true;
				crimeScence = jackKill();
			} else { // if Jack choose to wait
				currentTurn--;
				jackWait(markedToken);
				// Jack can reveal 1 patrol token.
				jackRevealToken(revealedPatrolToken, realPatrolToken, fakePatrolToken);
			}
		}
		return crimeScence;
	}
	/**
	 * get the Connected circle from the root location.
	 *
	 * @param root
	 *            current circle.
	 * @return the set of adjacent circles from the location.
	 */
	public final Set<String> getConnectedCircle(final String root) {
		Set<String> circleSet = new HashSet<String>();
		Set<Edge> edgeSet = gb.getCircleGraph().edgesOf(root);
		for (Edge s : edgeSet) {
			circleSet.add(s.getConnectedVertex(root));
		}
		return circleSet;
	}

	@Override
	protected final List<String> fakePatrolTokenGenerator() {
		List<String> fakePatrol = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.println("Fake Patrol Token:  ");
			String choseLocation = selectTargetedLocation(detectivesStartingLocation);
			fakePatrol.add(choseLocation);
			detectivesStartingLocation.remove(choseLocation);
		}
		return fakePatrol;
	}
	/**
	 * Ask jack to choose the crime scene from the marked token.
	 * @return chosen Crime Scene.
	 */
	private String jackKill() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Jack's turn: ");
		System.out.println("Jack is choosing Crime Scene:");
		String crimeScene = selectTargetedLocation(markedToken);
		indexOfCrimeScene = markedToken.indexOf(crimeScene);
		startingCrimeSceneLocation = startingMarkedToken.get(indexOfCrimeScene);
		markedToken.remove(crimeScene);
		return crimeScene;
	}
	/**
	 *  Move the marked locations one circle when jack chooses to wait.
	 *  Let Jack reveals one police token.
	 * @param markedToken the locations of marked Token Jack chosen.
	 */
	private void jackWait(final List<String> markedToken) {
		Scanner scan = new Scanner(System.in);
		for (int i = 0; i < markedToken.size(); i++) {
			String token = markedToken.get(i);
			System.out.println("Detectives turn: ");
			System.out.println("Choose one in the locations below to move the "
			+ markedToken.get(i) + " token: ");
			Set<String> adjacentCircles = getConnectedCircle(token);
			for (String s : adjacentCircles) {
				System.out.print(s + "  ");
			}
			String selectedLocation = scan.next();
			while (!adjacentCircles.contains(selectedLocation)) {
				System.out.println("Your choice is not in the list.");
				System.out.println("Please pick the square in the list:");
				selectedLocation = scan.next();
			}
			markedToken.set(i, selectedLocation);
		}
	}
	/**
	 * Reveal the patrol token in jack's waiting turn.
	 * @param revealedPatrolToken Token has been revealed.
	 * @param realPatrolToken real police patrol token.
	 * @param fakePatrolToken fake police patrol token.
	 */
	private void jackRevealToken(final List<String> revealedPatrolToken,
			final List<String> realPatrolToken,
			final List<String> fakePatrolToken) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Jack reveals one patrol token: ");
		System.out.println("Choose one of the police tokens below to reveal");
		List<String> allToken = new ArrayList<String>();
		allToken.addAll(realPatrolToken);
		allToken.addAll(fakePatrolToken);
		allToken.removeAll(revealedPatrolToken);
		// shuffle to ensure it is fair
		Collections.shuffle(allToken);
		for (String s : allToken) {
			System.out.print(s + "  ");
		}
		String input = scan.next();
		while (!allToken.contains(input)) {
			System.out.println("Your choice is not in the list.");
			System.out.println("Please pick the square in the list:");
			input = scan.next();
		}
		if (fakePatrolToken.contains(input)) {
			System.out.println(input + " is a fake Token!");
			System.out.println("Remove " + input + " from the board.");
			fakePatrolToken.remove(input);
		} else {
			int index = realPatrolToken.indexOf(input);
			System.out.println(input + " is a "
			+ detectives.getDetectives()[index].getColor() + " detective token");
			revealedPatrolToken.add(input);
		}
	}
	/**
	 * Ask user to select one location from a given set.
	 * @param possibleLocation set of possible choices.
	 * @return selected location.
	 */
	protected final String selectTargetedLocation(
			final Collection<String> possibleLocation) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose one location from the list below: ");
		for (String location : possibleLocation) {
			System.out.print(location + "  ");
		}
		System.out.println();
		String updatedLocation = sc.next();
		while (!possibleLocation.contains(updatedLocation)) {
			System.out.println("Your choice is not in the set."
					+ " Please select another one: ");
			updatedLocation = sc.next();
		}
		return updatedLocation;
	}
	/**
	 * Get the fake crime scene from Jack's decision.
	 * @return the fake crime scene.
	 */
	private String fakeCrimeSceneGenerator() {
		System.out.println("Please choose one of the locations"
				+ " below to be the fake Crime Scene:");
		String fakeCrimeScene = selectTargetedLocation(markedToken);
		markedToken.remove(fakeCrimeScene);
		return crimeScene;
	}
}
