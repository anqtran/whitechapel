package com.wingulabs.whitechapel.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConsoleUtility {

	private static Scanner sc;
	/**
	 * Get a location from a given set.
	 * 
	 * @param possibleLocation
	 *            set of possible choices.
	 * @return selected location.
	 */
	public static String selectLocationfromSet(final Set<String> possibleLocation) {
		List<String> listFromSet = new ArrayList<String>(possibleLocation);
		if(listFromSet.get(0).charAt(0) == 'C') {
			Collections.sort(listFromSet,cmp);
		} else {
			Collections.sort(listFromSet);
		}
		String updatedLocation = selectLocationfromList(listFromSet);
		return updatedLocation;
	}

	public static String selectLocationfromList(final List<String> possibleLocation) {
		System.out.println("Choose one location from the list below: ");
		printList(possibleLocation);
		int locationIndex = getIndexSelection(1, possibleLocation.size());
		String updatedLocation = possibleLocation.get(locationIndex - 1);
		return updatedLocation;
	}

	private static void printList(final List<String> possibleLocation) {
		List<String> locations = new ArrayList<String>(possibleLocation);
		int count = 0;
		for (int i = 0; i < locations.size(); i++) {
			System.out.printf("%-15s", (i+1) +"-"+ locations.get(i));
			count++;
			if (count == 10) {
				System.out.println();
				count = 0;
			}
		}
		System.out.println();
	}

	private static Comparator<String> cmp = new Comparator<String>() {
		public int compare(String o1, String o2) {
			String s1 = o1.substring(1, o1.length());
			String s2 = o2.substring(1, o2.length());
			return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
		}
	};

	public static int getIndexSelection(int lowerBound, int upperBound) {
	//	Scanner sc = getInputScannerFromFile();
		Scanner sc = new Scanner(System.in);
		int locationIndex;
		while (true)
			try {
				locationIndex = Integer.parseInt(sc.next());
				if (locationIndex < lowerBound || locationIndex > upperBound) {
					System.out.print("You entered an invalid choice, please try again: ");
				} else {
					break;
				}
			} catch (NumberFormatException nfe) {
				System.out.print("You did not enter a number, please try again: ");
			}
		return locationIndex;
	}

	public static List<String> getSearchCluesOrder(final Set<String> adjacentCircles) {
		Set<String> circles = new HashSet<String>(adjacentCircles);
		List<String> searchCluesOrder = new ArrayList<String>();
		while (!circles.isEmpty()) {
			String choice = selectLocationfromSet(circles);
			circles.remove(choice);
			searchCluesOrder.add(choice);
		}
		return searchCluesOrder;
	}
	public static Scanner getInputScannerFromFile() {
		if(sc == null) {
			try {
				sc = new Scanner(new File("tester_input.txt"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sc;
	}
}
