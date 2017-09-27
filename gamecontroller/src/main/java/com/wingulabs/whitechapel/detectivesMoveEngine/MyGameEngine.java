package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.util.ArrayList;
import java.util.List;

import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.DetectiveMove;
import com.wingulabs.whitechapel.detectives.Detectives;

/**
 * Class generates the logic of move detective.
 * @author anqtr
 *
 */
public class MyGameEngine implements GameEngine {

	@Override
	public final String getPriorityVertex(final Detectives loc,
											final MoveTree moveTree) {
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
		// TODO Auto-generated method stub
		List<DetectiveMove> moves = new ArrayList<DetectiveMove>();
		for(int i = 0; i< dts.getDetectives().length; i++ ) {
			moves.add(getMove(dts,mt,i));
		}
		return moves;
	}
                                                                                                                                                                                                                                                                                    
}

