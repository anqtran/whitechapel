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
import org.eareddick.whitechapel.MyDetectives;
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
	private MyDetectives detectives;
	/**
	 *  the board game.
	 */
	private GameBoard gb = GameBoard.SINGLETON;
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
		for (int i = 0; i < 5; i++) {
			detectives.getDetectivesMove().add(new ArrayList<String>());
			detectives.getDetectivesMove().get(i).add(detectives.getDetectives()[i].getLocation());
		}
		System.out.println("Night " + night.getCurrentNight() + " begins!!! ");
		int turn = night.getCurrentTurn();
		while (turn < 15) {
			turn++;
			night.setCurrentTurn(turn);
			System.out.println("Turn " + turn);
			jack.move(detectives);
			detectives.move(jack);
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
}

