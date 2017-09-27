package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.wingulabs.whitechapel.Utility.GraphUtility;
import com.wingulabs.whitechapel.detectives.DetectiveMoveResult;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.gameBoard.Answer;
import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;

/**
 * Move Tree to simulate Jack Move.
 * 
 * @author anqtr
 *
 */
public class MyMoveTree extends MoveTree {
	/**
	 * Constructor.
	 * 
	 * @param gameboard GameBoard from the library.
	 * @param rootLabel CrimeScene.
	 */
	public MyMoveTree(final GameBoard gameboard, final String rootLabel) {
		super(gameboard, rootLabel);
	}

	@Override
	public final void processJackMove(final Detectives detectives) {
		System.out.println("JACK MOVE!!!");
		updateLeaves(detectives);
	}

	/**
	 * For each element in leaves set, find connected vertex, create an edge
	 * between two and add the new vertex to the leaves set.
	 *
	 * @param currentLeaves current leaves set.
	 * @param detectives  detectives.
	 */
	private void updateLeaves(final Detectives detectives) {
		Set<Vertex> currentLeaves = new HashSet<>(this.leaves);
		// Clear the leave set
		leaves.clear();
		// For each element in the set
		for (Vertex currentLeaf : currentLeaves) {
			Set<String> newLeaves = GraphUtility.whereCanJackGo(currentLeaf.getLabel(), detectives, gameboard);
			connectDestinationsToOrigin(currentLeaf, newLeaves);
		}
	}

	private void connectDestinationsToOrigin(Vertex Origin, Set<String> Destinations) {
		for (String destinationLabel : Destinations) {
			Vertex destinationVertex = new Vertex(destinationLabel);
			addVertex(destinationVertex);
			leaves.add(destinationVertex);
			addEdge(Origin, destinationVertex);
		}
		
	}

	/**
	 * change the map according to the result of a detective's move.
	 *
	 */

	public final boolean processDetectiveMoveResultTester(String checkVertex, Answer clueAnswer) {
		// If the action is search clues
		if(!vertexIsInGraph(checkVertex)) {
			return false;
		}
		switch (clueAnswer) {
		case YES: // if answer is yes
			removeVertexYes(checkVertex);
			break;
		case NO: // if the answer is no
			removeVertexNo(checkVertex);
			break;
		}
		return false;
	}

	/**
	 * if the answer is yes, remove all the path that does not contains the
	 * targeted vertex.
	 * 
	 * @param currentRoot The crimescene.
	 * @param vertexToNotRemove vertex kept in the tree.
	 * @param vertexToRemove  vertex to remove.
	 */
	public final void removeVertexYes(String askedVertex) {
		boolean removeRoot = removeVertexYesHelper(root, askedVertex);
		if (removeRoot) {
			System.out.println("Jack is at " + root);
		}
	}
	private final boolean vertexIsInGraph(String askedVertex) {
		for(Vertex v : this.vertexSet()) {
			if(v.getLabel().equals(askedVertex)){
				return true;
			}
		}
		return false;
	}
	private final boolean removeVertexYesHelper(final Vertex currentV, String askedVertex) {
		
		if (currentV.getLabel().equals(askedVertex)) {
			currentV.setFoundClues(true);
			return false;
		}
		Set<Vertex> toRemove = new HashSet<>();
		Set<Edge> edges = outgoingEdgesOf(currentV);
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge edge = iter.next();
			Vertex connectedV = edge.getConnectedVertex(currentV);
			if (removeVertexYesHelper(connectedV, askedVertex)) {
				toRemove.add(connectedV);
			}
		}
		if (outgoingEdgesOf(currentV).size() == 0) {
			return true;
		}
		for(Vertex v : toRemove) {
			removeVertex(v); 
		}
		leaves.removeAll(toRemove);
		return false;
	}

	/**
	 * Remove Vertex at a specific location in the tree (used for NO Answer).
	 * @param currentRoot current vertex.
	 * @param vertextoRemove vertex to remove.
	 * @param leavesRemove leaves to remove.
	 */
	public final void removeVertexNo(String askedVertex) {
		removeVertexNoHelper(root, askedVertex);
	}

	private final boolean removeVertexNoHelper(final Vertex currentV, String askedVertex) {
		if (currentV.getLabel().equals(askedVertex)) {
			currentV.setFoundClues(false);
			return true;
		}
		Set<Vertex> toRemove = new HashSet<>();
		Set<Edge> edges = outgoingEdgesOf(currentV);
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge edge = iter.next();
			Vertex connectedV = edge.getConnectedVertex(currentV);
			if (removeVertexNoHelper(connectedV, askedVertex)) {
				toRemove.add(connectedV);
			}
		}
		if (outgoingEdgesOf(currentV).size() == 0) {
			return false;
		} 
		for(Vertex v : toRemove) {
			removeVertex(v);
		}
		leaves.removeAll(toRemove);
		return false;
	}

	@Override
	public boolean processDetectiveMoveResult(DetectiveMoveResult result) {
		// TODO Auto-generated method stub
		return false;
	}

	public void printTree(Vertex root) {
		System.out.println("Testing");
		System.out.println(root.getLabel());
		System.out.println(outgoingEdgesOf(root).size());
		if(outgoingEdgesOf(root).size() == 0) {
			System.out.println("Testing");
			return;
		}
		for(Edge e : outgoingEdgesOf(root)) {
			System.out.println("k");
			Vertex v = e.getConnectedVertex(root);
			System.out.print(v.getLabel() + " ");
			
			printTree(v);
		}
		System.out.println();
	}

}
