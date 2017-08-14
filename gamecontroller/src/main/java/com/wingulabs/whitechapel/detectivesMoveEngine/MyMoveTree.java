package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

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
	 * @param gameboard gameboard from the library.
	 * @param rootLabel Crimescence.
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
	 * @param currentLeavesSet current leaves set.
	 * @param detectives detectives.
	 */
	private void updateLeaves(final Set currentLeavesSet,
			final Detectives detectives) {
		Set<Vertex> currentLeaves = new HashSet<>(currentLeavesSet);
		// Clear the leave set
		currentLeavesSet.clear();
		// For each element in the set
		for (Vertex currentleaf : currentLeaves) {
			// Get connected edges
			Set<Edge> edgesSet = gameboard.getCircleGraph().
								edgesOf(currentleaf.getLabel());
			for (Edge s : edgesSet) {
				// For each edges, get the Vertex connected to the vertex in
				// leaf vertex
				String connectedleaf = s.getConnectedVertex(currentleaf.getLabel());
				// Check if there is any detective on the path between two
				// vertex
				// if (DetectiveCheck(currentleaf.getLabel(), connectedleaf,
				// detectives)) {
				// Create new leaf and new edges, add to the map and add to the
				// leaf set
				Vertex newleaf = new Vertex(connectedleaf);
				addVertex(newleaf);
				addEdge(currentleaf, newleaf);
				System.out.println(newleaf.getLabel());
				getLeaves().add(newleaf);
				// }
			}
		}
		System.out.println("------------");
	}

	// Check if there is a detective on the path ( I am still working on this.)
	private boolean DetectiveCheck(String v1, String v2, Detectives detectives) {
		DijkstraShortestPath<String, Edge> SP = new DijkstraShortestPath<>(gameboard.getMixedGraph());
		List<String> pathList = SP.getPath(v1, v2).getVertexList();
		for (String currentV : pathList) {
			for (Detective d : detectives.getDetectives()) {
				if (currentV.equals(d.getLocation())) {
					return false;
				}
			}
		}
		return true;
	}

	/** change the map according to the result of a detective's move.
	 *
	 */
	@Override
	public final void processDetectiveMoveResult(
			final DetectiveMoveResult result) {
		// If the action is search clues
		if (result instanceof SearchCluesResult) {
			Map<String, Answer> resultList =
							((SearchCluesResult) result).getClueAnswers(); //get answer list
			for (Map.Entry<String, Answer> clue :
				resultList.entrySet()) { // For each of the answer in the list
				String checkVertex = clue.getKey(); // get vertex and
													//answer in each element
				Answer clueAnswer = clue.getValue();
				switch (clueAnswer) {
				case YES: // if answer is yes
					Iterator leavesIter = getLeaves().iterator();
					// For each vertex in the leaves set, go up to the node.
					while (leavesIter.hasNext()) {
						removeVertexYes((Vertex) leavesIter.next(),
								checkVertex, new HashSet<Vertex>());
					}
					break;
				case NO: // if the answer is no
					removeVertexNo(root, checkVertex, new HashSet<Vertex>());
					break;
				default:
					break;
				}
			}
		}
		// if the action is attempt arrest
		if (result instanceof AttemptArrestResult) {
			String attemptedCircle = ((AttemptArrestResult) result).
									getMove().getTargetCircle();
			boolean jackIsThere = false;
			for (Vertex v: getLeaves()) {
				if (v.getLabel().equals(attemptedCircle)){
					jackIsThere = true;
					break;
				}
			}
			if (!jackIsThere) {
				System.out.println("There is no way Jack can be there! "
						+ "Try to search location in the possible location.");
				return;
			}
			// if the answer is yes, end game
			if (((AttemptArrestResult) result).getAnswer()) {
					System.out.println("You caught Jack! Mission accomplished. ");
			} else {
				System.out.println("Remove all the leaf vertex that contains "
									+ attemptedCircle);
				// If the answer is no, remove all the vertex in the map
				//and remove from the leaves list
				// This assumes that the detective only uses attempt arrest
				//when at the leaves vertex.
				Iterator leavesIter = getLeaves().iterator();
				while (leavesIter.hasNext()) {
					Vertex currentleaf = (Vertex) leavesIter.next();
					if (currentleaf.getLabel().equals(attemptedCircle)) {
						removeVertex(currentleaf);
						leavesIter.remove();
					}
				}
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
	public final void removeVertexYes(final Vertex currentRoot,
						final String vertexToNotRemove, final Set<Vertex> vertexToRemove) {
			if (currentRoot.equals(root)) { // when going up,
											//if a specific target is not found,
											//remove all the element in the set.
				return;
			}
			if (currentRoot.getLabel().equals(vertexToNotRemove)) {
				// if the vertex is found, keep that path.
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
	/**
	 * Recurssive method to remove the vertex answered yes.
	 * @param currentRoot current Location.
	 * @param vertexToNotRemove vertex to keep.
	 * @param vertexToRemove vertex to remove.
	 */
	public final void removeVertexYes2(final Vertex currentRoot,
			final String vertexToNotRemove, final Set<Vertex> vertexToRemove) {
		if (currentRoot == null) {
			Iterator leavesIter = getLeaves().iterator();
			removeVertexYes2((Vertex) leavesIter.next(),
					vertexToNotRemove, vertexToRemove);
		}
		if (currentRoot.getLabel().equals(vertexToNotRemove)) {
			return;
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
