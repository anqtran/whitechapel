package com.wingulabs.whitechapel.detectivesMoveEngine;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.wingulabs.whitechapel.detectives.DetectiveMoveResult;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree.Vertex;
import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;

/**
 * A tree / directed acyclic graph for tracking which paths Jack has potentially
 * made, through circle vertices. The root is a "kill spot". The leaves are the
 * vertices on which Jack might be at the current moment.
 *
 * @author Anwar Reddick
 *
 */
@SuppressWarnings("serial")
public abstract class MoveTree extends DirectedAcyclicGraph<Vertex, Edge> {

    /**
     * The vertex holding the "kill spot" label.
     */
    protected final Vertex root;

    /**
     * Vertices containing the position labels on which Jack might be at the
     * current moment.
     */
    protected Set<Vertex> leaves = new HashSet<>();

    /**
     * The GameBoard.
     */
    protected final GameBoard gameboard;

    /**
     * Initializes fields. Initializes the root vertex with rootLabel.
     * 
     * @param gameboard
     *            The GameBoard
     * @param rootLabel
     *            The label of the kill spot position.
     */
    public MoveTree(final GameBoard gameboard, final String rootLabel) {
        super(Edge.class);
        assert gameboard != null;
        assert rootLabel != null;
        this.gameboard = gameboard;
        root = new Vertex(rootLabel);
        addVertex(root);
        leaves.add(root);
        root.foundClues = true;
    }

    /**
     * Returns the leaves.
     * 
     * @return the leaves.
     */
    public final Set<Vertex> getLeaves() {
        return leaves;
    }

    /**
     * Returns the root vertex.
     * 
     * @return the root vertex.
     */
    public final Vertex getRoot() {
        return root;
    }
    public final void setLeaves(final Set<Vertex> destinations) {
        this.leaves = destinations;
    }

    /**
     * Updates this MoveTree to reflect the new positions that Jack could have
     * moved to, when doing a single, simple move. Ignore positions that Jack
     * cannot move through due to not being able to move through detectives.
     * <p>
     * For each leaf, find the adjacent circle vertices that Jack could have
     * moved to, and create edges from the old leaf to new MoveTree vertices.
     * Then update the {@link #leaves} field accordingly.
     * </p>
     * 
     * @param detectives
     *            The position of the detectives.
     */
    public abstract void processJackMove(Detectives detectives);

    /**
     * Updates this MoveTree by removing paths that Jack could not have possibly
     * taken based on result. Return True if Jack is caught.
     * 
     * @param result
     *            The result of a detective's questioning.
     */
    public abstract boolean processDetectiveMoveResult(DetectiveMoveResult result);

    /**
     * Models a vertex of a MoveTree.
     *
     * @author Anwar Reddick
     *
     */
    public static class Vertex {

        /**
         * The position label that matches a GameBoard circle vertex.
         */
        protected final String label;

        /**
         * Whether the detectives found clues at the position represented by
         * this vertex.
         */
        protected boolean foundClues;

        /**
         * Initializes fields.
         *
         * @param label
         *            The position label that matches a GameBoard circle vertex.
         */
        public Vertex(final String label) {
            this.label = label;
        }

        /**
         * Returns the label.
         * 
         * @return the label
         */
        public final String getLabel() {
            return label;
        }

        /**
         * Returns whether the detectives found clues at the position
         * represented by this vertex.
         * 
         * @return whether the detectives found clues at the position
         *         represented by this vertex.
         */
        public final boolean isFoundClues() {
            return foundClues;
        }

        /**
         * Sets whether the detectives found clues at the position represented
         * by this vertex.
         * 
         * @param foundClues
         *            whether the detectives found clues at the position
         *            represented by this vertex.
         */
        public final void setFoundClues(final boolean foundClues) {
            this.foundClues = foundClues;
        }

    }

    /**
     * Sets destinations as the leaves for this tree.
     * 
     * @param destinations
     *            The new leaves.
     */

}
