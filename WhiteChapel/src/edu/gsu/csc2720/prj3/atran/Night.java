package edu.gsu.csc2720.prj3.atran;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.eareddick.whitechapel.Detective;
import org.eareddick.whitechapel.Detectives;
import org.eareddick.whitechapel.Edge;
import org.eareddick.whitechapel.GameBoard;
/**
 * Class illustrates the night of White Chapel.
 * @author anqtr
 *
 */
public class Night {
	/**
	 * the crime scene.
	 */
	private String[] crimeScene;
	/**
	 * current turn of the night.
	 * there are 15 turns in one night.
	 */
	private int currentTurn;
	/**
	 * special tokens Jack has in each night.
	 */
	protected final int[] coachAlley;
	/**
	 * night number ( first, second, third or fourth).
	 */
	protected final int currentNight;
	/**
	 * Right and fake token that Jack can place at crime scene.
	 * Number of token to set up the game.
	 * the array varies in different night.
	 */
	protected final int[] womenToken; // the first element is marked token
										// the second is a false token
	/**
	 * detectives class.
	 */
	private Detectives detectives;
	/**
	 * String of faked token that Jack placed.
	 */
	private String[] fakeToken;
	/**
	 * String of marked token that Jack placed.
	 */
	private String[] markedToken;
	/**
	 * GameBoard of the game.
	 */
	private GameBoard gb = GameBoard.SINGLETON;
	/**
	 * all the crime scenes on the board.
	 */
	private Set<String> crimeSceneSet = gb.RED_CIRCLES;
	/**
	 * All the starting point of the detectives.
	 */
	private Set<String> detectivesStartingLocation = gb.YELLOW_SQUARES;
	/**
	 * Night Constructor.
	 * @param currentNight number of night.
	 */
	public Night(final int currentNight) {
		this.currentNight = currentNight;
		this.coachAlley = specialTokensGenerator(currentNight);
		this.womenToken = tokenGenerator(currentNight);
		this.markedToken = markedTokenGenerator();
		this.fakeToken = fakeTokenGenerator();
		this.detectives = detectivesGenerator();
		this.crimeScene = crimeSceneGenerator();
	}
	/**
	 * Another constructor used for night 2, 3 and 4
	 * In these night, detectives start at the previous locations.
	 * @param currentNight nunmber of night.
	 * @param dt detectives location from last night.
	 */
	public Night(final int currentNight, final Detectives dt) {
		this.currentNight = currentNight;
		this.coachAlley = specialTokensGenerator(currentNight);
		this.womenToken = tokenGenerator(currentNight);
		this.markedToken = markedTokenGenerator();
		this.fakeToken = fakeTokenGenerator();
		this.detectives = dt;
		this.crimeScene = crimeSceneGenerator();
	}
	/**
	 * Ask Jack to place the marked token to set up the night.
	 * @return the locations of all the marked token.
	 */
	private String[] markedTokenGenerator() {
		String[] markedToken = new String[womenToken[0]];
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < markedToken.length; i++) {
			System.out.println("Choose one location in the list below"
					+ " to place marked token : ");
			for (String crime : crimeSceneSet) {
				System.out.print(crime + "  ");
			}
			String choseToken = sc.next();
			markedToken[i] = choseToken;
			crimeSceneSet.remove(choseToken);
		}
		return markedToken;
	}
	/**
	 * Ask Jack to place the fake token to set up the night.
	 * @return the locations of fake token.
	 */
	private String[] fakeTokenGenerator() {
		Scanner sc = new Scanner(System.in);
		String[] fakeToken = new String[womenToken[1]];
		for (int i = 0; i < fakeToken.length; i++) {
			System.out.println("Choose one location in the"
								+ " list below to place fake token : ");
			for (String crime : crimeSceneSet) {
				System.out.print(crime + "  ");
			}
			String choseToken = sc.next();
			fakeToken[i] = choseToken;
			crimeSceneSet.remove(choseToken);
		}
		return fakeToken;
	}
	/**
	 * Place the detectives in different location at the beginning of the night.
	 * @return detectives.
	 */
	private Detectives detectivesGenerator() {
		Scanner sc = new Scanner(System.in);
		Detectives dt = new Detectives();
		Detective[] dts = dt.getDetectives();
		for (int i = 0; i < dts.length; i++) {
			System.out.println(dts[i].getColor() + " detective:");
			System.out.println("Choose one location in the list below: ");
			for (String location : detectivesStartingLocation) {
				System.out.print(location + "  ");
			}
			String choseLocation = sc.next();
			dts[i].setLocation(choseLocation);
			detectivesStartingLocation.remove(choseLocation);
		}
		return dt;
	}
	/**
	 * Get the numbers of coach and alley that Jack has in each night.
	 * @param currentNight current night.
	 * @return an array, first index is number of coach
	 * and second is number of alley.
	 */
	private int[] specialTokensGenerator(final int currentNight) {
		int[] a;
		switch (currentNight) {
		case 1:
			a = new int[] {3, 2 };
			break;
		case 2:
			a = new int[] {2, 2 };
			break;
		case 3:
			a = new int[] {2, 1 };
			break;
		case 4:
			a = new int[] {1, 1 };
			break;
		default:
			throw new IllegalArgumentException();
		}
		System.out.println("Welcome to Night " + currentNight);
		System.out.println("Jack has " + a[0] +
				" coach(es) and " + a[1] + " alley in this night.");
		return a;
	}
	/**
	 * Women's token and Fake Token jack has to start the game.
	 * @param currentNight number of night.
	 * @return first index is the marked token and second is the fake token.
	 */
	private int[] tokenGenerator(final int currentNight) {
		int[] a;
		switch (currentNight) {
		case 1:
			a = new int[] {5, 3 };
			break;
		case 2:
			a = new int[] {4, 3 };
			break;
		case 3:
			a = new int[] {3, 3 };
			break;
		case 4:
			a = new int[] {3, 1 };
			break;
		default:
			throw new IllegalArgumentException();
		}
		System.out.println("Jack starts with " + a[0]
				+ " women tokens and " + a[1] + " false tokens.");
		return a;
	}
	/**
	 * Generate the crime scene for each night.
	 * From the locations of all the tokens and detectives,
	 * ask Jack to wait or kill.
	 * If wait, let the detectives move all the marked token.
	 * @return the crime scene of the night.
	 */
	private String[] crimeSceneGenerator() {
		int numberofCS = 1;
		if (currentNight == 3)
			numberofCS = 2;
		String[] cs = new String[numberofCS];
		Scanner scan = new Scanner(System.in);
		boolean kill = false;
		while (!kill) {
			System.out.println("Jack, do you want to kill or wait? ");
			System.out.println("If you want to kill,"
					+ " enter k or press any button to wait. ");
			if (scan.next().equals("k")) { // if Jack choose to kill
				kill = true;
				System.out.println("Jack's turn: ");
				System.out.println("Please choose " +
				numberofCS + " from the locations below to be the Crime Scene:");
				for (int i = 0; i < markedToken.length; i++) {
					System.out.print(markedToken[i] + "  ");
				}
				for (int i = 0; i < cs.length; i++) {
					if (i != 0) {
						System.out.println("Choose the fake crime scene: ");
					}
					cs[i] = scan.next();
				}

			} else { // if Jack choose to wait
				currentTurn--;
				for (int i = 0; i < markedToken.length; i++) {
					System.out.println("Detectives turn: ");
					System.out.println("Choose one in the locations below to move the "
					+ markedToken[i] + " token: ");
					Set<String> adjacentCircles = getConnectedCircle(markedToken[i]);
					for (String s : adjacentCircles) {
						System.out.print(s + "  ");
					}
					markedToken[i] = scan.next();
				}
			}
		}
		return cs;
	}
	/** get the Connected circle from the root location.
	 * 
	 * @param root current circle.
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
	/**
	 * crime scene getter
	 * @return the crime scene.
	 */
	public final String[] getCrimeScene() {
		return crimeScene;
	}
	/**
	 * crime scene setter.
	 * @param crimeScene the crime scene;
	 */
	public final void setCrimeScene(final String[] crimeScene) {
		this.crimeScene = crimeScene;
	}
	/**
	 * current turn getter.
	 * @return current turn of the night.
	 */
	public final int getCurrentTurn() {
		return currentTurn;
	}
	/**
	 * Current turn setter.
	 * @param currentTurn new turn.
	 *
	 */
	public final void setCurrentTurn(final int currentTurn) {
		this.currentTurn = currentTurn;
	}
	/**
	 * Detectives getter.
	 * @return detectives.
	 */
	public final Detectives getDetectives() {
		return detectives;
	}
	/**
	 * detectives setter.
	 * @param detectives detectives.
	 */
	public final void setDetectives(final Detectives detectives) {
		this.detectives = detectives;
	}
	/**
	 * currentNight getter.
	 * @return currentNight.
	 */
	public final int getCurrentNight() {
		return currentNight;
	}
	/**
	 * coachAlley getter.
	 * @return array contains coach and alley.
	 */
	public final int[] getCoachAlley() {
		return coachAlley;
	}

}