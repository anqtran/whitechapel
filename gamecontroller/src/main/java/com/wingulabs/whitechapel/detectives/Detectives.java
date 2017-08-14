package com.wingulabs.whitechapel.detectives;

import com.wingulabs.whitechapel.detectives.Detective.DetectiveColor;

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
	}

	/**
	 * Returns the Detective objects.
	 * @return the Detective objects.
	 */
	public final Detective[] getDetectives() {
		return detectives;
	}


	/**
	 * Returns the Detective that's currently at the given location or null
	 * if no detectives are at that location.
	 * @param vertex The location to check.
	 * @return the Detective that's currently at the given location or null
	 * if no detectives are at that location.
	 */
	public final Detective isDetectiveOnLocation(final String vertex) {
		for (Detective det : detectives) {
			if (det.getLocation().equals(vertex))
				return det;
		}
		return null;
	}

}
