package com.wingulabs.whitechapel.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree;
import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree.Vertex;
import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;

public class DetectivesEngineUtility {
	public static final String SHORTEST_SQUARE_PATH = "shortest_square_paths.txt";
	private MoveTree mt;
	private NavigableSet<String> sortedFq;
	private GameBoard gb;
	private boolean[] assignedDestination;
	public DetectivesEngineUtility(MoveTree mt, boolean[] assignedDestination) {
		this.mt = mt;
		Map<String,Integer> m = vertexFrequencies();
		sortedFq = getSortedVertexFrequencies(m);
		gb = GameBoard.SINGLETON;
		this.assignedDestination = assignedDestination;

	}
	@SuppressWarnings("resource")
	public int getDistance(String origin, String destination) throws IOException {
		if(origin.equals(destination)) {
			return 0;
		}
		try(BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().getResource(SHORTEST_SQUARE_PATH).openStream()))) {
			String line = null;
			while((line = rdr.readLine()) != null) {
				if (line.isEmpty())
					continue;
				if(line.charAt(0) == 'S' && line.substring(19, line.length()).equals(origin)) {
					line = rdr.readLine();
					while(line.startsWith("D")) {
						String[] distanceParts = line.split("\\.");
						if(distanceParts[0].substring(distanceParts[0].indexOf('S'),
								distanceParts[0].length()).equals(destination)) {
							String[] distanceSmallPart = distanceParts[1].split(":");
							return Integer.parseInt(distanceSmallPart[1]);
						}
						line = rdr.readLine();
					}
				}
			}
		}
		return -1;
	}
	public LinkedList<String> getPreviousVertex(String source,String destination) throws IOException {
		LinkedList<String> previousV = new LinkedList<String>();
		try(BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().getResource(SHORTEST_SQUARE_PATH).openStream()))) {
			String line = null;
			while((line = rdr.readLine()) != null) {
				if (line.isEmpty())
					continue;
				if(line.charAt(0) == 'S' && line.substring(19, line.length()).equals(source)) {
					line = rdr.readLine();
					while(line.startsWith("D")) {
						String[] distanceParts = line.split("\\.");
						if(distanceParts[0].substring(distanceParts[0].indexOf('S'),
								distanceParts[0].length()).equals(destination)) {
							String[] distanceSmallPart = distanceParts[2].split(":");
							 previousV.add((distanceSmallPart[1]));
						}
						line = rdr.readLine();
					}
				}
			}
		}
		return previousV;
	}

	/**
	 * 
	 * @param dts
	 * @param priorityVertex
	 * @return the first element is the smallest distance, the second is te i
	 * @throws IOException
	 */
	public int[] getClosestDetective(Detectives dts, String priorityVertex) throws IOException {
		int minIndex = 0;
		int min = 99;
		Set<Edge> adjacentSquaresEdge = gb.getCircleToSquareGraph().edgesOf(priorityVertex);
		Set<String> adjacentSquares = new HashSet<String>();

		for(Edge e : adjacentSquaresEdge) {
			adjacentSquares.add(e.getConnectedVertex(priorityVertex));
		}
		for(String s : adjacentSquares ) {
			for(int i = 0 ; i< dts.getDetectives().length;i++) {
				if(assignedDestination[i]) {
					continue;
				}
				int d = getDistance(dts.getDetectives()[i].getLocation(),s);
				if( d < min) {
					min = d;
					minIndex = i;
				}
			}
		}
		assignedDestination[minIndex] = true;
		return new int[]{min,minIndex};
	}

	public LinkedList<String> getDetectivePath(String source, String destination) throws IOException {
		LinkedList<String> path = new LinkedList<String>();
		String squareDestination = getClosestSquareFromCircle(source,destination);
		path.add(squareDestination);
		return getDetectivePathHelper(source, squareDestination,path) ;
	}
	public LinkedList<String> getDetectivePathHelper(String source, String destination,LinkedList<String> path) throws IOException {
		if(source.equals(destination)) {
			path.removeLast();
			return path;
		}
		String prevVertex = getPreviousVertex(source,destination).removeFirst();
		System.out.println(prevVertex);
		path.add(prevVertex);
		return getDetectivePathHelper(source, prevVertex,path);
	}
	protected void SquareDistance() throws IOException {
		init();
	}

	private void init() throws IOException {


	}
	public Map<String,Integer> vertexFrequencies() {
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		countNumberOfVertexInTreeHelper(countMap,mt.getRoot());
		return countMap;
	}
	private void countNumberOfVertexInTreeHelper (Map<String,Integer> countMap,Vertex root) {
		if(countMap.containsKey(root.getLabel())) {
			int count = countMap.get(root.getLabel()) +1;
			countMap.put(root.getLabel(), count);
		} else {
			countMap.put(root.getLabel(), 1);
		}
		if(mt.outgoingEdgesOf(root).size() == 0) {
			return;
		}
		for(Edge e: mt.outgoingEdgesOf(root)) {
			Vertex v = e.getConnectedVertex(root);
			countNumberOfVertexInTreeHelper(countMap, v);
		}
	}
	private String getClosestSquareFromCircle(String startingSquare,String targetedCircle) throws IOException {
		Set<String> adjacentSquare = GraphUtility.getAdjacentSquarefromCircle(targetedCircle, gb);
		int min = 99;
		String square = "";
		for(String s : adjacentSquare) {
			int d = getDistance(startingSquare, s);
			if( d < min) {
				square = s;
			}
		}
		return square;
	}

	private NavigableSet<String> getSortedVertexFrequencies(Map<String,Integer> m) {
		ValueComparator bvc = new ValueComparator(m);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
		sorted_map.putAll(m);
		return sorted_map.descendingKeySet();
	}
	public String getMostFrequentVertex() {
		return sortedFq.pollLast();
	}
}
class ValueComparator implements Comparator<String> {
	Map<String, Integer> base;

	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}

