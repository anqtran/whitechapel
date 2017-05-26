package edu.gsu.csc2720.prj3.atran;

import java.util.HashSet;

import org.eareddick.whitechapel.Edge;
import org.eareddick.whitechapel.GameBoard;
import org.eareddick.whitechapel.MoveTree;
import org.eareddick.whitechapel.MoveTree.Vertex;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
/**
 * Square Map starts with current location and
 * generates all the square vertex in the middle
 * the leaves are adjacent circles.
 * @author anqtr
 *
 */
public class MySquareJackMoveTree extends DirectedAcyclicGraph<Vertex, Edge> {
	/**
	 * GameBoard.
	 */
	private GameBoard gb;
	/**
	 * Jack's current location.
	 */
	protected final Vertex root;
	/**
	 * Leaves are destination circles.
	 */
	protected HashSet<Vertex> leaves = new HashSet<>();
	/**
	 * Constructor.
	 * @param gameboard gameboard.
	 * @param rootLabel jack's current locations.
	 */
	public MySquareJackMoveTree(final GameBoard gameboard,
			final String rootLabel) {
		super(Edge.class);
		assert gameboard != null;
		assert rootLabel != null;
		this.gb = gameboard;
		root = new Vertex(rootLabel);
		addVertex(root);
		leaves.add(root);
		updateTree();
	}
	/**
	 *  update the tree according to the location.
	 */
	private void updateTree() {
	}
	/**
	 * Check if there is any detectives on the path from starting circle
	 * to the destination circle.
	 * @param endVertex destination circle.
	 * @return true if there is a detectives on the path.
	 */
	public final boolean detectivesOnPath(final String endVertex) {
		return true;
	}
}
