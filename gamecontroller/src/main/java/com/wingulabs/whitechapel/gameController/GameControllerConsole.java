package com.wingulabs.whitechapel.gameController;

import java.util.Scanner;

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
	 * Ask user to choose hideout from the entire game.
	 * @return Jack's hideout.
	 */
	protected String hideOutGenerator() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Jack is choosing his Hide Out: ");
		System.out.println("Choose any circle except the red circles: ");
		String hideOut = scan.next();
		while (!super.getGb().getCircleGraph().containsVertex(hideOut)) {
			System.out.println("Your choice is not a circle.");
			System.out.println("Please choose a circle: ");
			hideOut = scan.next();
		}
		while (super.getGb().RED_CIRCLES.contains(hideOut)) {
			System.out.println("You chose the red circle.");
			System.out.println("Please choose another circle: ");
			hideOut = scan.next();
		}
		return hideOut;
	}
}
