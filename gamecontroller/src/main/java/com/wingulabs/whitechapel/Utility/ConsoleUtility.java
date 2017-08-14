package com.wingulabs.whitechapel.Utility;

import java.util.Scanner;
import java.util.Set;

public class ConsoleUtility {

	/**
	 * Get a location from a given set.
	 * @param possibleLocation set of possible choices.
	 * @return selected location.
	 */
	public static String selectTargetedLocation(final Set<String> possibleLocation) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose one location from the list below: ");
		for (String location : possibleLocation) {
			System.out.print(location + "  ");
		}
		System.out.println();
		String updatedLocation = sc.next();
		while (!possibleLocation.contains(updatedLocation)) {
			System.out.println("Your choice is not in the set."
					+ " Please select another one: ");
			updatedLocation = sc.next();
		}
		return updatedLocation;
	}

}
