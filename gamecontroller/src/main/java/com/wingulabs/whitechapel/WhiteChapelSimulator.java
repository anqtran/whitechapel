package com.wingulabs.whitechapel;

import com.wingulabs.whitechapel.gameController.GameControllerConsole;

/**
 * Class starts the game.
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
	 * @param args **
	 */
	public static void main(final String[] args) {
		GameControllerConsole gs = new GameControllerConsole();
		gs.runSimpleNight();
	}
}
