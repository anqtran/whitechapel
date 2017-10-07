package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.DetectiveMove;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree.Vertex;
import com.wingulabs.whitechapel.gameBoard.Edge;

/**
 * Class generates the logic of move detective.
 * @author anqtr
 *
 */
public class MyGameEngine implements GameEngine {

	private MoveTree mt;
	private boolean[] setMove = new boolean[5];
	public MyGameEngine(MoveTree mt) {
		this.mt = mt;
	}
	@Override
	public final String getPriorityVertex(final Detectives loc,
											final MoveTree moveTree) {
		return null;
		
	}
	@Override
	public final DetectiveMove getMove(final Detectives loc,
								final MoveTree moveTree, final int detectiveIndex) {
		Detective dt = loc.getDetectives()[detectiveIndex];
		
		// get vertex from move tree with highest frequency == priority vertex
		// get shortest path from dt location to priority vertex
		
		return null;
		
	}
	@Override
	public List<DetectiveMove> setDetectiveMoves(Detectives dts, MoveTree mt) {
		List<DetectiveMove> moves = new ArrayList<DetectiveMove>();
		for(int i = 0; i< dts.getDetectives().length; i++ ) {
			moves.add(getMove(dts,mt,i));
		}
		return moves;
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
                                                                                                                                                                                                                                                                                    
}

