package com.wingulabs.whitechapel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.graph.SimpleGraph;

public class NightControllerConsole extends NightController{
	private GameBoard gb = GameBoard.SINGLETON;
	
	private boolean[] rushUsed;
	
	public NightControllerConsole(Night night, Jack jack, MyDetectives detectives, String hideOut) {
		super(night, jack, detectives, hideOut);
		rushUsed = new boolean[detectives.getDetectives().length];
	}

	@Override
	protected List<String> detectivesMove() {
		List finalDestination = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		System.out.println("Detectives' turn: ");
		Detective[] dts = detectives.getDetectives();
		// for each detective
		for (int i = 0; i < dts.length; i++) {
			String detectiveLocation = dts[i].getLocation();
			// Set contains option that detective can move.
			Set<String> moveChoices = new HashSet<String>();
			System.out.println(dts[i].getColor() + " detective's move:");
			System.out.println("Select one from the options below: ");
			System.out.println("\t 1- Stay or move 1 or 2 squares. ");
			System.out.println("\t 2- Rush (Move 3 squares without investigation). ");
			int choice = sc.nextInt();
			Set<String> adjacentLocation = getAdjacentVertex(detectiveLocation, 1);
			moveChoices.addAll(adjacentLocation);
			// Makes 2 squares move.
			for (String location : adjacentLocation) {
				moveChoices.addAll(getAdjacentVertex(location, 1));
			}
			// Rush
			if (choice == 2) {
				rushUsed[i] = true;
				//get first level
				Set<String> firstSquareSet = getAdjacentVertex(detectiveLocation, 1);
				Set<String> vertexInThreeMoves = new HashSet<String>();
				for (String firstSquare : firstSquareSet) {
					// get second levels
					Set<String> secondSquareSet = getAdjacentVertex(firstSquare, 1);
					 // add all the first and second vertex to the set.
					vertexInThreeMoves.addAll(secondSquareSet);
					for (String secondSquare : secondSquareSet) {
						Set<String> thirdSquareSet = getAdjacentVertex(secondSquare, 1);
						 // add all the second and third vertex.
						vertexInThreeMoves.addAll(thirdSquareSet);
					}
				}
				// keep the third level and remove all other vertex from the set.
				// this is rush option of jack.
				vertexInThreeMoves.removeAll(moveChoices);
				moveChoices = vertexInThreeMoves;
			}
			System.out.println("Choose one location from the list below: ");
			for (String location : moveChoices) {
				System.out.print(location + "  ");
			}
			System.out.println();
			String updatedLocation = sc.next();
			// update the past move of the detective.
			//set the detective location.
			finalDestination.add(updatedLocation);
			System.out.println();
		}
		return finalDestination;
	}

	@Override
	protected String jackMove() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Jack's turn: ");
		System.out.println("Choose one location from the list below: ");
		Set<String> adjacentLocation =
				getJackAdjacentVertex(jack.getCurrentLocation());
		for (String location : adjacentLocation) {
			System.out.print(location + "  ");
		}
		System.out.println();
		String updatedLocation = sc.next();
		return updatedLocation;
	}
	public final Set<String> getJackAdjacentVertex(final String root) {
		SimpleGraph<String, Edge> myGraph = gb.getCircleGraph();
		Set<String> vertexSet = new HashSet<String>();
		Set<Edge> edgeSet = myGraph.edgesOf(root);
		MySquareJackMoveTree squareMoveTree = new MySquareJackMoveTree(gb,root);
		for (Edge s : edgeSet) {
			// check if there is any detectives on the path
			String Destination = s.getConnectedVertex(root);
			if(!squareMoveTree.checkDetectivesOnPath(Destination)) {
			vertexSet.add(Destination);
			}
		}
		return vertexSet;
	}
	public final Set<String> getAdjacentVertex(final String root, final int a) {
		SimpleGraph<String, Edge> myGraph;
		 if (a == 1) {
			myGraph = gb.getSquareGraph();
		} else {
			myGraph = gb.getMixedGraph();
		}
		Set<String> vertexSet = new HashSet<String>();
		Set<Edge> edgeSet = myGraph.edgesOf(root);
		for (Edge s : edgeSet) {
			vertexSet.add(s.getConnectedVertex(root));
		}
		return vertexSet;
	}
}
