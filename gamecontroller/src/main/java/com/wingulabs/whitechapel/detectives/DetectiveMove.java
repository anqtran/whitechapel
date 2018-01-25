package com.wingulabs.whitechapel.detectives;

import java.util.Collections;
import java.util.List;

/**
 * Models a move that a Detective makes. This is also a container for nested
 * classes that actually model the moves.
 * 
 * @author Anwar Reddick
 *
 */
public interface DetectiveMove {

    /**
     * Moving three squares and not asking a question.
     * 
     * @author Anwar Reddick
     *
     */
    class Rush implements DetectiveMove {

        /**
         * The path of squares the detective moved.
         */
        private final List<String> path;

        /**
         * Initializes fields. Makes them unmodifiable.
         * 
         * @param path
         *            The path of squares the detective moved.
         */
        public Rush(final List<String> path) {
            this.path = Collections.unmodifiableList(path);
        }

        /**
         * Return The path of squares the detective moved.
         * 
         * @return The path of squares the detective moved.
         */
        public List<String> getPath() {
            return path;
        }
    }

    /**
     * A searching for clues move. Includes moving zero, one, or two squares.
     * 
     * @author Anwar Reddick
     *
     */
    class SearchClues implements DetectiveMove {

        /**
         * The order of circle vertices in which to search for clues.
         */
        private final List<String> searchOrder;

        /**
         * Initializes fields. Makes them unmodifiable.
         * 
         * @param searchOrder
         *            The order of circle vertices in which to search for clues.
         */
        public SearchClues(final List<String> searchOrder) {

            this.searchOrder = Collections.unmodifiableList(searchOrder);
        }

        /**
         * Returns The order of circle vertices in which to search for clues.
         * 
         * @return The order of circle vertices in which to search for clues.
         */
        public List<String> getSearchOrder() {
            return searchOrder;
        }

    }

    /**
     * An arrest attempt. Includes moving zero, one, or two squares.
     * 
     * @author Anwar Reddick
     *
     */
    class AttemptArrest implements DetectiveMove {

        /**
         * The location to attempt an arrest.
         */
        private final String targetCircle;

        /**
         * Initializes fields. Makes path unmodifiable.
         * 
         * @param targetCircle
         *            The location to attempt an arrest.
         */
        public AttemptArrest(final String targetCircle) {
            this.targetCircle = targetCircle;
        }

        /**
         * Returns The location to attempt an arrest.
         * 
         * @return The location to attempt an arrest.
         */
        public String getTargetCircle() {
            return this.targetCircle;
        }

    }
}
