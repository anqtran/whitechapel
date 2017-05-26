package edu.gsu.csc2720.prj3.atran;

import java.util.ArrayList;
import org.eareddick.whitechapel.Detective;
/**
 * My detectives class.
 * @author anqtr
 *
 */
public class MyDetective extends Detective {
	/**
	 * List of past moves of the detective.
	 */
	private ArrayList<String> detectivesMove;
	/**
	 * Constructor.
	 * @param color color of detective.
	 */
	public MyDetective(final DetectiveColor color) {
		super(color);
		detectivesMove = new ArrayList<String>();
	}
	/**
	 * Get the list of past move.
	 * @return past move list.
	 */
	public final ArrayList<String> getDetectivesMove() {
		return detectivesMove;
	}
}
