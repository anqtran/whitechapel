package com.wingulabs.whitechapel.Utility;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.SimpleGraph;

import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;

public class GraphUtility {
	/**
	 *  Get adjacent vertex from the graph.
	 * @param root starting location.
	 * @param myGraph the graph.
	 * @return set of adjacent location.
	 */
	public static Set<String> getAdjacentVertex(final String root,
			final SimpleGraph<String, Edge> myGraph) {
		Set<String> vertexSet = new HashSet<String>();
		Set<Edge> edgeSet = myGraph.edgesOf(root);
		for (Edge s : edgeSet) {
			vertexSet.add(s.getConnectedVertex(root));
		}
		return vertexSet;
	}
	/**
	 * Get adjacent circle on the graph.
	 * @param root the root location.
	 * @return set of adjacent circle.
	 */
	public static Set<String> getAdjacentCircle(final String root, GameBoard gb) {
		SimpleGraph<String, Edge> circleGraph = gb.getMixedGraph();
		return getAdjacentVertex(root, circleGraph);
	}
	/**
	 * Get adjacent square on the graph.
	 * @param root the root location.
	 * @return set of adjacent square.
	 */
	public static Set<String> getAdjacentSquare(final String root, GameBoard gb) {
		SimpleGraph<String, Edge> squareGraph = gb.getSquareGraph();
		return getAdjacentVertex(root, squareGraph);
	}
}
