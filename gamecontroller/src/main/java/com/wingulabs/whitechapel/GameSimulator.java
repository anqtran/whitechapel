package com.wingulabs.whitechapel;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.wingulabs.whitechapel.Detective.DetectiveColor;

/**
 * Class runs and controls the game.
 * @author anqtr
 *
 */
public class GameSimulator {
	private List<DetectiveColor> InvestigationPile;
	
	public GameSimulator() {
		InvestigationPile = new LinkedList<DetectiveColor>() {{
		add(DetectiveColor.BLUE);
		add(DetectiveColor.RED);
		add(DetectiveColor.GREEN);
		add(DetectiveColor.BROWN);
		add(DetectiveColor.YELLOW);
		}};
	}
	public void run() {
		GameBoard gb = GameBoard.SINGLETON;
		Set<String> startingRedCircles = gb.RED_CIRCLES;
		Set<String> startingSquare = gb.YELLOW_SQUARES;
		getHeadofInvestigation();
		runFirstNight(startingRedCircles, startingSquare);
		


	}
	private boolean runFirstNight(Set<String> startingRedCircles,
			Set<String> startingSquare ) {
		Night firstNight = new NightConsole(1,startingSquare,startingRedCircles);
		return true;
	}
	private boolean runSecondFouthNight(Set<String> lastDetectivesLocation) {
		return true;
	}
	private boolean runThirdNight(Set<String> lastDetectivesLocation) {
		return true;
	}
	private void getHeadofInvestigation() {
		Random rn = new Random();
		int i = rn.nextInt(InvestigationPile.size());
		DetectiveColor dColor = InvestigationPile.get(i);
		InvestigationPile.remove(i);
		System.out.println(dColor + " Detective is head of investigation in this night!");
	}
}
