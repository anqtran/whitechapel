package com.wingulabs.whitechapel.nightController;

import java.util.List;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;
import com.wingulabs.whitechapel.night.Night;
/**
 * Class to control all activities in the night.
 * @author anqtr
 *
 */
public class NightController {

	protected AbstractJackController jackController;
	protected AbstractDetectiveController detectiveController;
	
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
	protected Detectives detectives;
	/**
	 *  the board game.
	 */
	protected GameBoard gb = GameBoard.SINGLETON;
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
			final Detectives detectives, final String hideOut) {
		super();
		this.night = night;
		this.jack = jack;
		this.detectives = detectives;
		this.hideOut = hideOut;
		this.jackController = new JackConsoleController(gb,jack,night);
		this.detectiveController = new DetectiveConsoleController(gb,detectives);	
	}
	/**
	 * Start the night.
	 * @return true if jack successfully reaches the hideout.
	 */
	public final boolean run() {
		System.out.println("Night " + night.getCurrentNight() + " begins!!! ");
		while (night.getCurrentTurn() < 15) {
			int turn = night.getCurrentTurn() + 1;
			night.setCurrentTurn(turn);
			System.out.println("Turn " + night.getCurrentTurn());
			String jackMove = jackController.jackMove();
			jack.setCurrentLocation(jackMove);
			if (jack.getCurrentLocation().equals(hideOut)) {
				System.out.println("Jack got to his hide out, night "
						+ (int) (night.getCurrentNight() + 1) + " will start soon...");
				return true;
			}
			if (turn == 15) {
				System.out.println("Jack cannot reach his Hide Out in the night"
						+ ". Game Over!");
				return false;
			}
			List<String> detectivesUpdateLocation = detectiveController.detectivesMove();
			for (int i = 0; i < detectives.getDetectives().length; i++) {
				detectives.getDetectives()[i].setLocation(
						detectivesUpdateLocation.get(i));
			}
			boolean caughtJack = detectiveController.investigation(jack);
			if (caughtJack) {
				System.out.println("Jack was caught by the detectives. Game Over!");
				return false;
			}
			System.out.println("Turn " + night.getCurrentTurn() + " ends. ");
		}
		return false;
	}
	/**
	 * Jack's getter.
	 * @return Jack.
	 */
	public final Jack getJack() {
		return jack;
	}
	/**
	 * Detectives getter.
	 * @return detectives.
	 */
	public final Detectives getDetectives() {
		return detectives;
	}
	/**
	 * Night getter.
	 * @return night.
	 */
	public final Night getNight() {
		return this.night;
	}
}

