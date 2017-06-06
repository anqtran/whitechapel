package com.wingulabs.whitechapel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
/**
 * Class illustrates the night of White Chapel.
 * @author anqtr
 *
 */
public abstract class Night {
	/**
	 * the crime scene.
	 */
	protected List<String> crimeScene;
	/**
	 * current turn of the night.
	 * there are 15 turns in one night.
	 */
	protected int currentTurn;
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
	protected MyDetectives detectives;
	/**
	 * String of real token that detective placed.
	 */
	protected List<String> realPatrolToken = new ArrayList<String>();
	/**
	 * String of faked token that detective placed.
	 */
	protected List<String> fakePatrolToken;
	/**
	 * String of faked token that Jack placed.
	 */
	protected Set<String> fakeToken;
	/**
	 * String of marked token that Jack placed.
	 */
	protected List<String> markedToken;
	/**
	 * GameBoard of the game.
	 */
	private GameBoard gb = GameBoard.SINGLETON;

	public Night(int currentNight) {
		this.currentNight = currentNight;
		this.currentTurn = 0;
		this.coachAlley = specialTokensGenerator(currentNight);
		this.womenToken = tokenGenerator(currentNight);
	}
	
	/**
	 * @param crimeScene
	 * @param currentTurn
	 * @param coachAlley
	 * @param currentNight
	 * @param womenToken
	 * @param detectives
	 * @param fakePatrolToken
	 * @param fakeToken
	 * @param markedToken
	 * @param gb
	 */
	public Night(List<String> crimeScene, int currentTurn, int[] coachAlley, int currentNight, int[] womenToken,
			MyDetectives detectives, List<String> fakePatrolToken, Set<String> fakeToken, List<String> markedToken,
			GameBoard gb) {
		super();
		this.crimeScene = crimeScene;
		this.currentTurn = currentTurn;
		this.coachAlley = coachAlley;
		this.currentNight = currentNight;
		this.womenToken = womenToken;
		this.detectives = detectives;
		this.fakePatrolToken = fakePatrolToken;
		this.fakeToken = fakeToken;
		this.markedToken = markedToken;
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
	 * Ask Jack to place the marked token to set up the night.
	 * @return the locations of all the marked token.
	 */
	protected abstract List<String> markedTokenGenerator();

	/**
	 * Ask Jack to place the fake token to set up the night.
	 * @return the locations of fake token.
	 */
	protected abstract Set<String> fakeTokenGenerator();
	/**
	 * Place the detectives in different location at the beginning of the night.
	 * @return detectives.
	 */
	protected abstract MyDetectives detectivesGenerator();
	/**
	 * Generate the crime scene for each night.
	 * From the locations of all the tokens and detectives,
	 * ask Jack to wait or kill.
	 * If wait, let the detectives move all the marked token.
	 * @return the crime scene of the night.
	 */
	protected abstract List<String> crimeSceneGenerator();
	/**
	 * Ask detective to place the fake token to set up the night.
	 * @return the locations of fake token.
	 */
	protected abstract List<String> fakePatrolTokenGenerator();
	/**
	 * crime scene getter
	 * @return the crime scene.
	 */
	public final List<String> getCrimeScene() {
		return crimeScene;
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
	public final MyDetectives getDetectives() {
		return detectives;
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
