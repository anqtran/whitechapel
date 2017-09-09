package com.wingulabs.whitechapel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.DetectiveMove;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree;
import com.wingulabs.whitechapel.detectivesMoveEngine.MyMoveTree;
import com.wingulabs.whitechapel.gameBoard.Answer;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.gameController.GameControllerConsole;

/**
 * Class starts the game.
 * 
 * @author anqtr
 *
 */
public final class WhiteChapelSimulator {
	/**
	 * Private constructor prevents extension.
	 */
	private WhiteChapelSimulator() {
	}

	/**
	 * Main class to start the game.
	 * 
	 * @param args
	 * **
	 */
	public static void main(final String[] args) {
		GameBoard gb = GameBoard.SINGLETON;
		MyMoveTree mt = new MyMoveTree(gb, "C65");

		Detectives dt = initDetectives();
		mt.processJackMove(dt);
		mt.processDetectiveMoveResultTester("C66", Answer.YES);
		// GameControllerConsole gs = new GameControllerConsole();
		// gs.runSimpleNight();
	}

	private static Detectives initDetectives() {
		Detectives dt = new Detectives();
		List<String> detectivesLocations = new ArrayList<String>(GameBoard.SINGLETON.YELLOW_SQUARES);
		detectivesLocations.remove("SC101S1");
		detectivesLocations.remove("SC130S1");

		Detective[] dts = dt.getDetectives();
		for (int i = 0; i < dts.length; i++) {
			String location = detectivesLocations.get(i);
			System.out.println(dts[i].getColor() + " detective is at: " + location);
			dts[i].setLocation(location);
		}
		return dt;
	}
}
