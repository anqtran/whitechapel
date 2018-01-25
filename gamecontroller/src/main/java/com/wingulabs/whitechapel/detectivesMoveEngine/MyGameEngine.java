package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wingulabs.whitechapel.Utility.DetectivesEngineUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;

/**
 * Class generates the logic of move detective.
 * 
 * @author anqtr
 *
 */
public class MyGameEngine implements GameEngine {

    private MoveTree moveTree;
    private DetectivesEngineUtility dUtility;
    private Detectives dt;
    private boolean[] assignedDestination = new boolean[5];

    public MyGameEngine(MoveTree mt, Detectives dt) {
        this.moveTree = mt;
        this.dt = dt;
    }

    @Override
    public final String getPriorityVertex(final Detectives loc, final MoveTree moveTree) {

        return dUtility.getMostFrequentVertex();

    }

    @Override
    public final String getMove(final String pVertex, final Detectives loc, final DetectivesEngineUtility dUtility,
            final int detectiveIndex) throws IOException {
        Detective dt = loc.getDetectives()[detectiveIndex];
        LinkedList<String> path = dUtility.getDetectivePath(dt.getLocation(), pVertex);
        String nextVertex = "";
        for (int i = 0; i < 2; i++) {
            nextVertex = path.removeLast();
            if (nextVertex.equals(dt.getLocation())) {
                return pVertex;
            }
        }
        return nextVertex;

    }

    @Override
    public List<String> setDetectiveMoves(Detectives dts, MoveTree mt) throws IOException {
        List<String> moves = new ArrayList<String>();
        dUtility = new DetectivesEngineUtility(mt, assignedDestination);
        int countUnassignedDetective = 0;
        for (int i = 0; i < assignedDestination.length; i++) {
            if (!assignedDestination[i]) {
                countUnassignedDetective++;
            }
        }
        for (int i = 0; i < countUnassignedDetective; i++) {
            String pVertex = getPriorityVertex(dt, moveTree);
            int[] closestD = dUtility.getClosestDetective(dts, pVertex);
            int detectiveIndex = closestD[1];
            assignedDestination[detectiveIndex] = true;
            moves.add(getMove(pVertex, dts, dUtility, detectiveIndex));
        }
        return moves;
    }

}
