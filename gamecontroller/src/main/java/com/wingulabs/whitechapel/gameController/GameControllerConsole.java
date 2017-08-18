package com.wingulabs.whitechapel.gameController;

import java.util.HashSet;
import java.util.Set;

import com.wingulabs.whitechapel.Utility.ConsoleUtility;

/**
 * Control the flow of the game.
 * @author anqtr
 *
 */
public class GameControllerConsole extends GameController {
	
	/**
	 * 
	 */
	public GameControllerConsole() {
		super();
		
	}

	/**
	 * Ask user to Select hideout from the entire game.
	 * @return Jack's hideout.
	 */
	protected String hideOutGenerator() {
		System.out.println("Jack is choosing his Hide Out: ");
		System.out.println("Select any circle except the red circles: ");
		Set<String> CirclesExceptRed = new HashSet<String>(super.getGb().getCircleGraph().vertexSet());
		CirclesExceptRed.removeAll(super.getGb().RED_CIRCLES);
		String hideOut = ConsoleUtility.selectLocationfromSet(CirclesExceptRed);
		return hideOut;
	}
}
