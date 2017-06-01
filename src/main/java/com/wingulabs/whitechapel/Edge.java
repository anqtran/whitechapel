package com.wingulabs.whitechapel;

import org.jgrapht.graph.DefaultEdge;

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
	public String getConnectedVertex(final String sourceVertex) {
		if (super.getSource().equals(sourceVertex))
			return (String) super.getTarget();
		else if (super.getTarget().equals(sourceVertex))
			return (String) super.getSource();
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
	public String getTargetVertex(final String sourceVertex) {
		if (super.getSource().equals(sourceVertex))
			return (String) super.getTarget();
		else
			throw new RuntimeException("Vertex: " + sourceVertex
					+ " is not a source of edge: " + this);
	}
}
