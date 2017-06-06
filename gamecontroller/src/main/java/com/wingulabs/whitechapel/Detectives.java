package com.wingulabs.whitechapel;

import java.util.ArrayList;
import java.util.List;

import com.wingulabs.whitechapel.Detective.DetectiveColor;

/**
 * Contains the Detective objects, including their locations.
 * @author Anwar Reddick
 *
 */
public class Detectives {

	/**
	 * The array of Detective objects.
	 */
	protected final Detective[] detectives;

	/**
	 * List of past moves of the detective.
	 */
	private List<String> detectivesMove;
	
	/**
	 * Initializes fields.
	 */
	public Detectives() {
		detectives = new Detective[]{
				new Detective(DetectiveColor.BLUE),
				new Detective(DetectiveColor.BROWN),
				new Detective(DetectiveColor.GREEN),
				new Detective(DetectiveColor.RED),
				new Detective(DetectiveColor.YELLOW)
		};
		detectivesMove = new ArrayList<String>();

	}

	/**
	 * Returns the Detective objects.
	 * @return the Detective objects.
	 */
	public Detective[] getDetectives() {
		return detectives;
	}
	
	public List<String> getDetectivesMove() {
		return detectivesMove;
	}


	/**
	 * Returns the Detective that's currently at the given location or null
	 * if no detectives are at that location.
	 * @param vertex The location to check.
	 * @return the Detective that's currently at the given location or null
	 * if no detectives are at that location.
	 */
	public Detective isDetectiveOnLocation(final String vertex) {
		for (Detective det : detectives) {
			if (det.getLocation().equals(vertex))
				return det;
		}
		return null;
	}

}
