package com.wingulabs.whitechapel;

import java.util.ArrayList;
import java.util.List;
/**
 * Class to control all activities in the night.
 * @author anqtr
 *
 */
public abstract class NightController {
	/**
	 * current Night.
	 */
	protected Night night;
	/**
	 * Jack.
	 */
	protected Jack jack;
	/**
	 * detectives.
	 */
	protected MyDetectives detectives;
	/**
	 *  the board game.
	 */
	protected GameBoard gb = GameBoard.SINGLETON;
	/**
	 * The move tree to process Jack move.
	 */
	protected MoveTree mt;
	/**
	 * Jack Hide out.
	 */
	protected String hideOut;
	/**
	 * Night Controller constructor.
	 * @param night current Night.
	 * @param jack Jack.
	 * @param detectives detectives.
	 * @param hideOut Jack's hide out.
	 */
	public NightController(final Night night, final Jack jack,
			final MyDetectives detectives, final String hideOut) {
		super();
		this.night = night;
		this.jack = jack;
		this.detectives = detectives;
		mt = new MyMoveTree(GameBoard.SINGLETON, jack.getCurrentLocation());
		this.hideOut = hideOut;
	}
	/**
	 * Start the night.
	 * @return true if jack successfully reaches the hideout.
	 */
	public final boolean run() {
		System.out.println("Night " + night.getCurrentNight() + " begins!!! ");
		int turn = night.getCurrentTurn();
		while (turn < 15) {
			turn++;
			night.setCurrentTurn(turn);
			System.out.println("Turn " + turn);
			detectivesMove();
			jackMove();
			boolean caughtJack = detectives.investigation(jack, mt);
			// if jack reaches the hideouts, start new night.
			if (caughtJack) {
				System.out.println("Jack was caught by the detectives. Game Over!");
				return false;
			}
			if (jack.getCurrentLocation().equals(hideOut)) {
				System.out.println("Jack got to his hide out, night "
						+ night.getCurrentNight() + 1 + " will start soon...");
				return true;
			} else {
				if (turn == 15) {
					System.out.println("Jack cannot reach his hideOut in the night"
							+ ". Game Over!");
					return false;
				}
			}
			System.out.println("Turn " + turn + " ends. ");
		}
		return false;
	}
	/**
	 * process the detectives move.
	 */
	abstract List<String> detectivesMove();
	/**
	 * process the jack move.
	 */
	abstract String jackMove();
	
}

