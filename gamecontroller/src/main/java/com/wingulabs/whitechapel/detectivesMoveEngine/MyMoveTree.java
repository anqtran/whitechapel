package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import com.wingulabs.whitechapel.Utility.GraphUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.DetectiveMoveResult;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectives.DetectiveMoveResult.AttemptArrestResult;
import com.wingulabs.whitechapel.detectives.DetectiveMoveResult.SearchCluesResult;
import com.wingulabs.whitechapel.gameBoard.Answer;
import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
/**
 * Move Tree to simulate Jack Move.
 * @author anqtr
 *
 */
public class MyMoveTree extends MoveTree {
	/**
	 * Constructor.
	 * @param gameboard GameBoard from the library.
	 * @param rootLabel CrimeScene.
	 */
	public MyMoveTree(final GameBoard gameboard, final String rootLabel) {
		super(gameboard, rootLabel);
	}

	@Override
	public final void processJackMove(final Detectives detectives) {
		updateLeaves(getLeaves(), detectives);
		for(Vertex v :getLeaves()) {
			System.out.println(v.getLabel());
		}

	}

	/**
	 * For each element in leaves set, find connected vertex, create an edge
	 * between two and add the new vertex to the leaves set.
	 *
	 * @param currentLeaves current leaves set.
	 * @param detectives detectives.
	 */
	private void updateLeaves(final Set currentLeaves, final Detectives detectives) {
		Set<Vertex> oldLeaves = new HashSet<>(currentLeaves);
		// Clear the leave set
		currentLeaves.clear();
		// For each element in the set
		for (Vertex currentLeaf : oldLeaves) {
			Set<String> newLeaves = GraphUtility.whereCanJackGo(currentLeaf.getLabel(), detectives, gameboard);
			connectDestinationsToOrigin(currentLeaf, newLeaves);
			Set<Vertex> leavesVertex = new HashSet<Vertex>();
			for(String location: newLeaves) {
				leavesVertex.add(new Vertex(location));
			}
			getLeaves().addAll(leavesVertex);
			}
		System.out.println("------------");
	}
	private void connectDestinationsToOrigin(Vertex Origin, Set<String> Destinations) {
		for( String destinationLabel : Destinations) {
		Vertex destinationVertex = new Vertex(destinationLabel);
		addVertex(destinationVertex);
		addEdge(Origin, destinationVertex);
		}
	}

	/** change the map according to the result of a detective's move.
	 *
	 */
	
	public final boolean processDetectiveMoveResultTester(String checkVertex, Answer clueAnswer) {
		// If the action is search clues
				switch (clueAnswer) {
				case YES: // if answer is yes
					removeVertexYes(checkVertex);
					break;
				case NO: // if the answer is no
					removeVertexNo(checkVertex);
					break;
				}
				return true;
	}

	/**
	 * if the answer is yes, remove all the path that
	 * does not contains the targeted vertex. 
	 * @param currentRoot The crimescene.
	 * @param vertexToNotRemove vertex kept in the tree.
	 * @param vertexToRemove vertex to remove.
	 */
	public final void removeVertexYes(String askedVertex) {
		removeVertexYesHelper(root, askedVertex);
		}

	private final void removeVertexYesHelper(final Vertex currentV, String askedVertex) {
			if(currentV.getLabel().equals(askedVertex)) {
				return;
			}
			if(outgoingEdgesOf(currentV).size() == 0) {
				removeVertex(currentV);
				return;
			}
			Set<Edge> edges = edgesOf(currentV);
			for (Edge edge : edges) {
				Vertex connectedV = edge.getConnectedVertex(currentV);
				removeVertexYesHelper(connectedV, askedVertex);
				if(outgoingEdgesOf(currentV).size() == 0) {
					return;
				}
			}
	}
	/**
	 * Remove Vertex at a specific location in the tree (used for NO Answer).
	 * @param currentRoot current vertex.
	 * @param vertextoRemove vertex to remove.
	 * @param leavesRemove leaves to remove.
	 */
	public final void removeVertexNo(String askedVertex) {
		removeVertexYesHelper(root, askedVertex);
		}

	private final void removeVertexNoHelper(final Vertex currentV, String askedVertex) {
			if(currentV.getLabel().equals(askedVertex)) {
				removeVertex(currentV);
				return;
			}
			if(outgoingEdgesOf(currentV).size() == 0) {
				return;
			}
			Set<Edge> edges = edgesOf(currentV);
			for (Edge edge : edges) {
				Vertex connectedV = edge.getConnectedVertex(currentV);
				removeVertexYesHelper(connectedV, askedVertex);
				if(outgoingEdgesOf(currentV).size() == 0) {
					return;
				}
			}
	}

	@Override
	public boolean processDetectiveMoveResult(DetectiveMoveResult result) {
		// TODO Auto-generated method stub
		return false;
	}
	public void printTree(){
		Set<Edge> edges = edgesOf(root);
		for (Edge edge : edges) {
			Vertex connectedV = edge.getConnectedVertex(root);
			
		}
		}
	}
}
