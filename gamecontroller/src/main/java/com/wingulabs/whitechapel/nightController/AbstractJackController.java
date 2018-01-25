package com.wingulabs.whitechapel.nightController;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.SimpleGraph;

import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;
import com.wingulabs.whitechapel.jack.MySquareJackMoveTree;
import com.wingulabs.whitechapel.night.Night;

/**
 * Makes decisions for Jack.
 * 
 * @author anwar
 *
 */
public abstract class AbstractJackController {

    protected final GameBoard gb;
    protected final Jack jack;
    protected final Night night;

    public AbstractJackController(final GameBoard gb, final Jack jack, Night night) {
        this.gb = gb;
        this.jack = jack;
        this.night = night;
    }

    /**
     * Get possible destination of Jack on the board.
     * 
     * @param root
     *            the root location.
     * @return set of adjacent square.
     */
    protected Set<String> getJackAdjacentVertex(final String root) {
        SimpleGraph<String, Edge> myGraph = gb.getCircleGraph();
        Set<String> vertexSet = new HashSet<String>();
        Set<Edge> edgeSet = myGraph.edgesOf(root);
        MySquareJackMoveTree squareMoveTree = new MySquareJackMoveTree(gb, root);
        for (Edge s : edgeSet) {
            // check if there is any detectives on the path
            String destination = s.getConnectedVertex(root);
            if (!squareMoveTree.checkDetectivesOnPath(destination)) {
                vertexSet.add(destination);
            }
        }
        return vertexSet;
    }

    /**
     * process the jack move.
     * 
     * @return destination of Jack.
     */
    abstract String jackMove();

}
