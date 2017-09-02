package com.wingulabs.whitechapel.gameBoard;

import org.jgrapht.graph.DefaultEdge;

import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree.Vertex;

/**
 * Extends the JGraphT DefaultEdge with convenience methods.
 * @author Anwar Reddick
 *
 */
@SuppressWarnings("serial")
public class Edge extends DefaultEdge {

	/**
	 * Returns the vertex that's connected / adjacent to sourceVertex.
	 * @param sourceVertex The vertex for which to return the connected /
	 * adjacent vertex.
	 * @return the vertex that's connected / adjacent to sourceVertex.
	 */
	public final String getConnectedVertex(final String sourceVertex) {
		if (super.getSource().equals(sourceVertex))
			return (String) super.getTarget();
		else if (super.getTarget().equals(sourceVertex))
			return (String) super.getSource();
		else
			throw new RuntimeException("Vertex: " + sourceVertex
					+ " is not a member of edge: " + this);
	}
	public final Vertex getConnectedVertex(final Vertex sourceVertex) {
		if (super.getSource().equals(sourceVertex))
			return (Vertex) super.getTarget();
		else if (super.getTarget().equals(sourceVertex))
			return (Vertex) super.getSource();
		else
			throw new RuntimeException("Vertex: " + sourceVertex
					+ " is not a member of edge: " + this);
	}
	/**
	 * For directed edges, returns the destination vertex for a source vertex.
	 * @param sourceVertex The vertex for which to return the connected /
	 * adjacent vertex.
	 * @return the destination vertex for a source vertex.
	 */
	public final String getTargetVertex(final String sourceVertex) {
		if (super.getSource().equals(sourceVertex))
			return (String) super.getTarget();
		else
			throw new RuntimeException("Vertex: " + sourceVertex
					+ " is not a source of edge: " + this);
	}
}
