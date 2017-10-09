package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wingulabs.whitechapel.Utility.DetectivesEngineUtility;
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

	private MoveTree moveTree;
	private DetectivesEngineUtility dUtility;
	private Detectives dt;
	private boolean[] setMove = new boolean[5];
	public MyGameEngine(MoveTree mt,Detectives dt) {
		this.moveTree = mt;
		this.dt = dt;
		dUtility = new DetectivesEngineUtility(mt);
	}
	@Override
	public final String getPriorityVertex(final Detectives loc, final MoveTree moveTree) {
		
		String s  = dUtility.getMostFrequentVertex();
		try {
			System.out.println(dUtility.getClosestDetective(dt, s)[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	@Override
	public final DetectiveMove getMove(final Detectives loc,
								final MoveTree moveTree, final int detectiveIndex) {
		Detective dt = loc.getDetectives()[detectiveIndex];
	
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
                                                                                                                                                                                                                                                                         
}

