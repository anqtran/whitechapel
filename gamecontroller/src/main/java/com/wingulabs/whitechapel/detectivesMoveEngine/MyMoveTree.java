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
	@Override
	public final boolean processDetectiveMoveResult(
			final DetectiveMoveResult result) {
		// If the action is search clues
		if (result instanceof SearchCluesResult) {
			Map<String, Answer> resultList =
							((SearchCluesResult) result).getClueAnswers(); //get answer list
			for (Map.Entry<String, Answer> clues : resultList.entrySet()) { // For each of the answer in the list
				String checkVertex = clues.getKey(); // get vertex and answer in each element
				Answer clueAnswer = clues.getValue();
				switch (clueAnswer) {
				case YES: // if answer is yes
					removeVertexYes();
					break;
				case NO: // if the answer is no
					removeVertexNo();
					break;
				}
			}
			return false;
		}
		// if the action is attempt arrest
		else {
			String attemptedCircle = ((AttemptArrestResult) result).
									getMove().getTargetCircle();
			// if the answer is yes, end game
			if (((AttemptArrestResult) result).getAnswer()) {
					return true;
			} else {
				System.out.println("Remove all the leaf vertex that contains "
									+ attemptedCircle);
				getLeaves().remove(attemptedCircle);
				return false;
					}
				}
			}
	/**
	 * if the answer is yes, remove all the path that
	 * does not contains the targerted vertex. ( We am still working on this.)
	 * @param currentRoot The crimescene.
	 * @param vertexToNotRemove vertex kept in the tree.
	 * @param vertexToRemove vertex to remove.
	 */
	public final void removeVertexYes(final Vertex currentVertex, Set<Vertex> leaves, String askedVertex,boolean remove) {
			if (currentVertex.equals(root)) {
				return;
			}
			if(currentVertex.getLabel().equals(askedVertex)) {
				return;
			}
			vertexToRemove.add(currentRoot); // add vertex to the set.
			Iterator descendantIter = getDescendants(this, currentRoot).iterator();
			// Since the getdescendants return a set,
			//we need to iterate through the set to get the descendant's element.
			while (descendantIter.hasNext()) {
			removeVertexYes((Vertex) descendantIter.next(),
							vertexToNotRemove, vertexToRemove);
			}
			for (Vertex v : vertexToRemove) {
				removeVertex(v);
			}
			getLeaves().removeAll(vertexToRemove);
	}
	public final void removeBranch(Vertex root) { 
		Set<Edge> edges = this.edgesOf(root);
		if(edges.size() == 0) {
			
		}
		for( Edge e : edges) {
			Vertex v = e.getConnectedVertex(root);
			
		}
		
	}


	/**
	 * Remove Vertex at a specific location in the tree (used for NO Answer).
	 * @param currentRoot current vertex.
	 * @param vertextoRemove vertex to remove.
	 * @param leavesRemove leaves to remove.
	 */
	private void removeVertexNo(final Vertex currentRoot,
			final String vertextoRemove, final Set<Vertex> leavesRemove) {
		// Remove currentRoot vertex from the tree if it matches with the
		// removeVertex.
		if (currentRoot.getLabel().equals(vertextoRemove)
				&& !currentRoot.equals(root)) {
			// Remove vertex from the leaves set
			if (getLeaves().contains(currentRoot)) {
				getLeaves().remove(currentRoot);
			}
			// Remove vertex from the map.
			removeVertex(currentRoot);
			return;
		}
		if (getDescendants(this, currentRoot) == null) {
			return;
		}
		Iterator connectedIter = getDescendants(this, currentRoot).iterator();
		while (connectedIter.hasNext()) {
			Vertex nextVertex = (Vertex) connectedIter.next();
			removeVertexNo(nextVertex, vertextoRemove, leavesRemove);
		}
	}
}
