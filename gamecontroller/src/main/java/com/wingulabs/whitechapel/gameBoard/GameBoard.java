package com.wingulabs.whitechapel.gameBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.SimpleGraph;

/**
 * Contains three graphs that model the game board. One graph captures the
 * circle-to-circle edges, most of which pass through squares. This graph is
 * used to track Jack's potential movements. Another graph captures the
 * square-to-square edges, some of which pass through circles. This graph is
 * used to determine potential detective movements. The last graph captures the
 * square-to-circle edges. This graph is used to track potential questions that
 * detectives can ask.
 * <p>This class uses the <a
 * href="https://en.wikipedia.org/wiki/Singleton_pattern">singleton pattern</a>
 * </p>
 *
 * @author Anwar Reddick
 *
 */
public class GameBoard {

	/**
	 * File containing circle-to-circle edge data.
	 */
	public static final String CIRCLE_TO_CIRCLE_FILEPATH =
			"circle_to_circle_edges.txt";

	/**
	 * File containing square-to-square and square-to-circle edge data.
	 */
	public static final String PRIMARY_EDGES_FILEPATH = "edges.txt";

	/**
	 * File containing alley circle-to-circle edge data.
	 */
	public static final String ALLEY_CIRCLE_TO_CIRCLE_FILEPATH =
			"alley_edges.txt";

	/**
	 * The graph containing all edges (to/from squares and circles)
	 */
	private final SimpleGraph<String, Edge> completeGraph =
			new SimpleGraph<>(Edge.class);
	
	/**
	 * The graph containing square-to-square edges.
	 */
	private final SimpleGraph<String, Edge> squareGraph =
			new SimpleGraph<>(Edge.class);

	/**
	 * The graph containing square-to-circle edges.
	 */
	private final SimpleGraph<String, Edge> mixedGraph =
			new SimpleGraph<>(Edge.class);

	/**
	 * The graph containing circle-to-circle edges.
	 */
	private final SimpleGraph<String, Edge> circleGraph =
			new SimpleGraph<>(Edge.class);

	/**
	 * The graph containing circle-to-circle alley edges.
	 */
	private final SimpleGraph<String, Edge> alleyCircleGraph =
			new SimpleGraph<>(Edge.class);

	/**
	 * The square vertices that have the yellow border.
	 */
	public static final Set<String> YELLOW_SQUARES = new HashSet<>();

	/**
	 * The red circle vertices.
	 */
	public static final Set<String> RED_CIRCLES = new HashSet<>();

	/**
	 * The GameBoard Singleton.
	 */
	public static final GameBoard SINGLETON;

	static {
		try {
			SINGLETON = new GameBoard();
		} catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		}

		YELLOW_SQUARES.add("SC136E4");
		YELLOW_SQUARES.add("SC98S2");
		YELLOW_SQUARES.add("SC101S1");
		YELLOW_SQUARES.add("SC29E1");
		YELLOW_SQUARES.add("SC34S1");
		YELLOW_SQUARES.add("SC40S1");
		YELLOW_SQUARES.add("SC130S1");

		RED_CIRCLES.add("C3");
		RED_CIRCLES.add("C21");
		RED_CIRCLES.add("C27");
		RED_CIRCLES.add("C65");
		RED_CIRCLES.add("C84");
		RED_CIRCLES.add("C147");
		RED_CIRCLES.add("C149");
		RED_CIRCLES.add("C158");
	}

	/**
	 * Reads the data files and initializes the three graphs. Public
	 * instantiation is prevented. See SINGLETON.
	 * @throws IOException If an error occurs while reading the data files.
	 */
	protected GameBoard() throws IOException {
		init();
		initCompleteGraph();
		
	}

	/**
	 * Reads the data files and initializes the three graphs.
	 * @throws IOException If an error occurs while reading the data files.
	 */
	private void init() throws IOException {
		try (BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().
				getResource(PRIMARY_EDGES_FILEPATH).openStream()))) {

			String line = null;
			while ((line = rdr.readLine()) != null) {
				//System.out.println(line);
				if (line.isEmpty())
					continue;
				if (line.startsWith("#"))
					continue;
				String[] edgeParts = line.split(":");
				if (edgeParts.length != 2) {
					throw new RuntimeException("Invalid line in edges.txt: " + line);
				}

				String vertex1 = edgeParts[0];
				String vertex2 = edgeParts[1];

				// Ignore circle-to-circle edges; those are handled later
				if (vertex1.startsWith("C") && vertex2.startsWith("C"))
					continue;

				// If one vertex is a circle, then the edge goes in the mixed
				//  graph.
				if (vertex1.startsWith("C") || vertex2.startsWith("C")) {
					mixedGraph.addVertex(vertex1);
					mixedGraph.addVertex(vertex2);
					mixedGraph.addEdge(vertex1, vertex2);
				}

				// If both vertices are squares, then the edge goes in the
				//  square graph.
				else {
					squareGraph.addVertex(vertex1);
					squareGraph.addVertex(vertex2);
					squareGraph.addEdge(vertex1, vertex2);
				}
			}
		}

		fixSquareGraph();


		try (BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().
				getResource(CIRCLE_TO_CIRCLE_FILEPATH).openStream()))) {

			String line = null;
			while ((line = rdr.readLine()) != null) {
				//System.out.println(line);
				if (line.startsWith("#"))
					continue;

				if (line.startsWith(":")) {
					String sourceVertex = "C" + line.substring(1, line.length());
					readAdjacentCircleVerticies(sourceVertex, rdr);
				}
			}
		}
		initAlleyCircleGraph();

	}
	
	private void initCompleteGraph() throws IOException {
		try (BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().
				getResource(PRIMARY_EDGES_FILEPATH).openStream()))) {

			String line = null;
			while ((line = rdr.readLine()) != null) {
				//System.out.println(line);
				if (line.isEmpty())
					continue;
				if (line.startsWith("#"))
					continue;
				String[] edgeParts = line.split(":");
				if (edgeParts.length != 2) {
					throw new RuntimeException("Invalid line in edges.txt: " + line);
				}

				String vertex1 = edgeParts[0];
				String vertex2 = edgeParts[1];
				
				completeGraph.addVertex(vertex1);
				completeGraph.addVertex(vertex2);
				completeGraph.addEdge(vertex1, vertex2);
			}
		}
	}
	
	/**
	 * Reads the data files and process the graph.
	 * @throws IOException If an error ocurrs throw exception.
	 */
	private void initAlleyCircleGraph() throws IOException {
		try (BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().
				getResource(ALLEY_CIRCLE_TO_CIRCLE_FILEPATH).openStream()))) {

			String line = null;
			while ((line = rdr.readLine()) != null) {
				//System.out.println(line);
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				if (!line.startsWith("C"))
					throw new RuntimeException("Detected invalid line: '" + line + "'");

				String[] parts = line.split(":");
				if (parts.length != 2)
					throw new RuntimeException("Detected invalid line: '" + line + "'");
				String sourceVertex = parts[0];
				String[] destVertices = parts[1].split(",");
				for (String destVertex : destVertices) {
					alleyCircleGraph.addVertex(sourceVertex);
					alleyCircleGraph.addVertex(destVertex);
					alleyCircleGraph.addEdge(sourceVertex, destVertex);
				}
			}
		}
	}

	/**
	 * From the circle-to-circle data file, processes circle vertices that are
	 * adjacent to a given source circle vertex.
	 *
	 * @param sourceVertex The source vertex
	 * @param rdr The file reader
	 * @throws IOException Upon an error occurring when reading the data file.
	 */
	private void readAdjacentCircleVerticies(final String sourceVertex,
			final BufferedReader rdr) throws IOException {

		circleGraph.addVertex(sourceVertex);

		String line = null;
		while ((line = rdr.readLine()) != null) {
			if (line.isEmpty())
				break;

			if (line.startsWith("#"))
				continue;
			String targetVertex = "C" + line;
			circleGraph.addVertex(targetVertex);

			if (circleGraph.containsEdge(sourceVertex, targetVertex))
				throw new RuntimeException("Duplicate edge detected.");
			circleGraph.addEdge(sourceVertex, targetVertex);
		}
	}

	/**
	 * Processes the square-to-circle graph to detect square-to-square edges
	 * that go through circle vertices.
	 */
	private void fixSquareGraph() {

		for (String sourceVertex : mixedGraph.vertexSet()) {
			// Ignore circle vertices
			if (sourceVertex.startsWith("C"))
				continue;

			// We now have a square vertex.
			assert sourceVertex.startsWith("S");

			// Determine adjacent square vertices.
			// All direct edges lead to circle vertices;
			//  find the square vertices on the other side of those
			//  circle vertices.
			Set<Edge> edges = mixedGraph.edgesOf(sourceVertex);

			for (Edge edge : edges) {
				String circleVertex = edge.getConnectedVertex(sourceVertex);
				assert circleVertex.startsWith("C");

				for (Edge circleEdge : mixedGraph.edgesOf(circleVertex)) {
					String targetVertex = circleEdge.getConnectedVertex(circleVertex);
					assert targetVertex.startsWith("S");

					// Don't try to add a circular edge.
					if (sourceVertex.equals(targetVertex))
						continue;

					squareGraph.addVertex(sourceVertex);
					squareGraph.addVertex(targetVertex);
					squareGraph.addEdge(sourceVertex, targetVertex);
				}
			}
		}
	}

	/**
	 * Returns the circle graph.
	 * @return the circle graph.
	 */
	public final SimpleGraph<String, Edge> getCircleGraph() {
		return circleGraph;
	}

	/**
	 * Returns the square graph.
	 * @return the square graph.
	 */
	public final SimpleGraph<String, Edge> getSquareGraph() {
		return squareGraph;
	}

	/**
	 * Returns the mixed graph.
	 * @return the mixed graph.
	 */
	public final SimpleGraph<String, Edge> getMixedGraph() {
		return mixedGraph;
	}

	/**
	 * Returns the alley circle graph.
	 * @return the alley circle graph.
	 */
	public final SimpleGraph<String, Edge> getAlleyCircleGraph() {
		return alleyCircleGraph;
	}

	public SimpleGraph<String, Edge> getCompleteGraph() {
		return completeGraph;
	}
	

}
