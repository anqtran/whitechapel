package com.wingulabs.whitechapel;

/**
 * A Whitechapel Detective.
 * @author Anwar Reddick
 *
 */
public class Detective {

	/**
	 * Detective Color.
	 * @author Anwar Reddick
	 *
	 */
	public enum DetectiveColor {

		/**
		 * blue.
		 */
		BLUE,

		/**
		 * green.
		 */
		GREEN,

		/**
		 * brown.
		 */
		BROWN,

		/**
		 * yellow.
		 */
		YELLOW,

		/**
		 * red.
		 */
		RED;
	}

	/**
	 * The color of the detective.
	 */
	private final DetectiveColor color;

	/**
	 * The GameBoard vertex where the detective is.
	 */
	private String location;

	/**
	 * Initializes the detective color.
	 * @param color the detective color.
	 */
	public Detective(final DetectiveColor color) {
		this.color = color;
	}

	/**
	 * Returns the detective's location.
	 * @return the detective's location.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the detective's location.
	 * @param location the detective's location.
	 */
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * Returns the detective's color.
	 * @return the detective's color.
	 */
	public DetectiveColor getColor() {
		return color;
	}
}
