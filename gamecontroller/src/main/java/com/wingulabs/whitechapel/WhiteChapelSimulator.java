package com.wingulabs.whitechapel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;

import com.wingulabs.whitechapel.Utility.DetectivesEngineUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectivesMoveEngine.MyGameEngine;
import com.wingulabs.whitechapel.detectivesMoveEngine.MyMoveTree;
import com.wingulabs.whitechapel.gameBoard.GameBoard;

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
	 * *
	 * @throws IOException *
	 */
	public static void main(final String[] args) throws IOException {
		GameBoard gb = GameBoard.SINGLETON;
		DetectivesEngineUtility dUtility = new DetectivesEngineUtility();
//		System.out.println(dPath.getDistance("SC186E1", "SC31W1S1"));
		MyMoveTree mt = new MyMoveTree(gb, "C65");
		Detectives dt = initDetectives();
		mt.processJackMove(dt);
//		mt.processDetectiveMoveResultTester("C66", Answer.NO);
		mt.processJackMove(dt);
		mt.processJackMove(dt);
		mt.processJackMove(dt);
		mt.processJackMove(dt);
		MyGameEngine ge = new MyGameEngine(mt);
		Map<String,Integer> m = ge.vertexFrequencies();
		for(String s : m.keySet()) {
			System.out.println(s + " " + m.get(s));
		}
		NavigableSet<String> sortedFq = dUtility.getSortedSet(m);
		System.out.println(sortedFq.pollLast());
		System.out.println(sortedFq.pollLast());
		System.out.println(sortedFq.pollLast());
		
//		mt.processDetectiveMoveResultTester("C66", Answer.NO);

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
