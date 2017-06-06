package com.wingulabs.whitechapel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Class illustrates the night of White Chapel.
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
	 * Night Constructor.
	 * 
	 * @param currentNight
	 *            number of night.
	 * @param detectivesStartingLocation
	 *            starting locations of detectives.
	 * @param crimeSceneSet
	 *            starting locations of Jack.
	 */
	public NightConsole(int currentNight, Set<String> detectivesStartingLocation, Set<String> crimeSceneSet) {
		super(currentNight);
		this.detectivesStartingLocation = detectivesStartingLocation;
		this.crimeSceneSet = crimeSceneSet;
		this.markedToken = markedTokenGenerator();
		this.fakeToken = fakeTokenGenerator();
		this.detectives = detectivesGenerator();
		this.fakePatrolToken = fakePatrolTokenGenerator();
		this.crimeScene = crimeSceneGenerator();
	}

	/**
	 * Ask Jack to place the marked token to set up the night.
	 * 
	 * @return the locations of all the marked token.
	 */
	protected List<String> markedTokenGenerator() {
		System.out.println("Setting up Crime Scene! ");
		List<String> markedToken = new ArrayList<>();
		int numberOfMarkedToken = womenToken[0];
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < numberOfMarkedToken; i++) {
			System.out.println("Choose one location in the list below" + " to place marked token : ");
			for (String crime : crimeSceneSet) {
				System.out.print(crime + "  ");
			}
			String choseToken = sc.next();
			while (!crimeSceneSet.contains(choseToken)) {
				System.out.println("Your choice is not in the list.");
				System.out.println("Please pick the circle in the list:");
				choseToken = sc.next();
			}
			markedToken.add(choseToken);
			crimeSceneSet.remove(choseToken);
		}
		return markedToken;
	}

	/**
	 * Ask Jack to place the fake token to set up the night.
	 * 
	 * @return the locations of fake token.
	 */
	protected Set<String> fakeTokenGenerator() {
		Scanner sc = new Scanner(System.in);
		Set<String> fakeToken = new HashSet<String>();
		int numberOfFakeToken = womenToken[1];
		for (int i = 0; i < numberOfFakeToken; i++) {
			System.out.println("Choose one location in the" + " list below to place fake token : ");
			for (String crime : crimeSceneSet) {
				System.out.print(crime + "  ");
			}
			String choseToken = sc.next();
			while (!crimeSceneSet.contains(choseToken)) {
				System.out.println("Your choice is not in the list.");
				System.out.println("Please pick the circle in the list:");
				choseToken = sc.next();
			}
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
	protected MyDetectives detectivesGenerator() {
		Scanner sc = new Scanner(System.in);
		MyDetectives dt = new MyDetectives();
		Detective[] dts = dt.getDetectives();
		for (int i = 0; i < dts.length; i++) {
			System.out.println(dts[i].getColor() + " detective:");
			System.out.println("Choose one location in the list below: ");
			for (String location : detectivesStartingLocation) {
				System.out.print(location + "  ");
			}
			String choseLocation = sc.next();
			while (!detectivesStartingLocation.contains(choseLocation)) {
				System.out.println(" Your square is not in the list.");
				System.out.println("Please pick the square in the list:");
				choseLocation = sc.next();
			}
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
	protected List<String> crimeSceneGenerator() {
		int numberofCS = 1;
		if (currentNight == 3)
			numberofCS = 2;
		List<String> cs = new ArrayList<String>();
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
				List<String> temp = jackKill(numberofCS);
				cs.addAll(temp);
			} else { // if Jack choose to wait
				currentTurn--;
				jackWait(markedToken);	
				// Jack can reveal 1 patrol token.
				jackRevealToken(revealedPatrolToken, realPatrolToken, fakePatrolToken);
			}
		}
		return cs;
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
	protected List<String> fakePatrolTokenGenerator() {
		List<String> fakePatrol = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.println("Fake Patrol Token:  ");
			System.out.println("Choose one location in the list below: ");
			for (String location : detectivesStartingLocation) {
				System.out.print(location + "  ");
			}
			String choseLocation = sc.next();
			while (!detectivesStartingLocation.contains(choseLocation)) {
				System.out.println(" Your square is not in the list.");
				System.out.println("Please pick the square in the list:");
				choseLocation = sc.next();
			}
			fakePatrol.add(choseLocation);
			detectivesStartingLocation.remove(choseLocation);
		}
		return fakePatrol;
	}

	private List<String> jackKill(int numberofCS) {
		List<String> cs = new ArrayList<String>();
		Scanner scan = new Scanner(System.in);
		System.out.println("Jack's turn: ");
		System.out.println("Please choose " + numberofCS + " from the locations below to be the Crime Scene:");
		for (int i = 0; i < markedToken.size(); i++) {
			System.out.print(markedToken.get(i) + "  ");
		}
		for (int i = 0; i < numberofCS; i++) {
			if (i != 0) {
				System.out.println("Choose the fake crime scene: ");
			}
			String input = scan.next();
			while (!markedToken.contains(input)) {
				System.out.println("Your choice is not in the list.");
				System.out.println("Please pick the square in the list:");
				input = scan.next();
			}
			cs.add(input);
		}
		return cs;
	}
	private void jackWait(List<String> markedToken) {
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
			String input = scan.next();
			while(! adjacentCircles.contains(input)) {
				System.out.println("Your choice is not in the list.") ;
				System.out.println("Please pick the square in the list:");
				input = scan.next();
			}
			markedToken.set(i,input);
		}
	}
	private void jackRevealToken(List<String> revealedPatrolToken,
			List<String> realPatrolToken,List<String> fakePatrolToken){
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
		while(! allToken.contains(input)) {
			System.out.println("Your choice is not in the list.") ;
			System.out.println("Please pick the square in the list:");
			input = scan.next();
		}
		if(fakePatrolToken.contains(input)) {
			System.out.println(input + " is a fake Token!");
			System.out.println("Remove " + input + " from the board.");
			fakePatrolToken.remove(input);
		} else {
			int index = realPatrolToken.indexOf(input);
			System.out.println(input + " is a " +
			detectives.getDetectives()[index].getColor() + " detective token");
			revealedPatrolToken.add(input);
		}
	}
}
