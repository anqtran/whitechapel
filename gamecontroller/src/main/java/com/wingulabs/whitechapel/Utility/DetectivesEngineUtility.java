package com.wingulabs.whitechapel.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree;
import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree.Vertex;
import com.wingulabs.whitechapel.gameBoard.Edge;

public class DetectivesEngineUtility {
	public static final String SHORTEST_SQUARE_PATH = "shortest_square_paths.txt";
	private MoveTree mt;
	private NavigableSet<String> sortedFq;
	public DetectivesEngineUtility(MoveTree mt) {
		this.mt = mt;
		Map<String,Integer> m = vertexFrequencies();
		sortedFq = getSortedSet(m);

	}
	@SuppressWarnings("resource")
	public int getDistance(String origin, String destination) throws IOException {
		try(BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().getResource(SHORTEST_SQUARE_PATH).openStream()))) {
			String line = null;
			while((line = rdr.readLine()) != null) {
				System.out.println(line);
				if (line.isEmpty())
					continue;
				if(line.charAt(0) == 'S' && line.substring(19, line.length()).equals(origin)) {
					line = rdr.readLine();
					Scanner sc = new Scanner(line);
					sc.useDelimiter("\\.");
					String[] distanceParts = new String[3];
					for(int i = 0; i < 3;i++) {
						distanceParts[i] = sc.next();
						System.out.println(distanceParts[i]);
					}
					System.out.println(distanceParts[0].substring(distanceParts[0].indexOf('S'),
							distanceParts[0].length()));
					System.out.println(destination);
					if(distanceParts[0].substring(distanceParts[0].indexOf('S'),
							distanceParts[0].length()).equals(destination)) {
						String[] distanceSmallPart = distanceParts[1].split(":");
						System.out.println("Yes");
						return Integer.parseInt(distanceSmallPart[1]);
					}
				}
			}
		}
		return -1;
	}
	public Set<String> previousVertex(String source,String destination) {
		Set<String> previousV = new HashSet<String>();
		try(BufferedReader rdr = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().getResource(SHORTEST_SQUARE_PATH).openStream()))) {
			String line = null;
			while((line = rdr.readLine()) != null) {
				if(line.substring(19, line.length()).equals(source)) {
					String[] distanceParts = line.split(".");
					if(distanceParts[0].substring(distanceParts[0].indexOf('S'),
							distanceParts[0].length()).equals(destination)) {
						String[] distanceSmallParts = distanceParts[2].split(":");
						previousV.add(distanceSmallParts[1]);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		for(int i = 0 ; i< dts.getDetectives().length;i++) {
			int d = getDistance(dts.getDetectives()[i].getLocation(),priorityVertex);
			if( d < min) {
				min = d;
				minIndex = i;
			}
		}
		return new int[]{min,minIndex};
	}

	public List<String> getDetectivePath(String source, String destination) {
		return null;
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
          
	private NavigableSet<String> getSortedSet(Map<String,Integer> m) {
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

