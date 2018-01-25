package com.wingulabs.whitechapel.detectives;

import java.util.Collections;
import java.util.Map;

import com.wingulabs.whitechapel.gameBoard.Answer;

/**
 * Models the result of a detective move, including results from asking a
 * question. This is also a container for nested classes that actually model the
 * results.
 *
 * @author Anwar Reddick
 *
 */
public interface DetectiveMoveResult {

    /**
     * Models the result of searching for clues.
     * 
     * @author Anwar Reddick
     *
     */
    class SearchCluesResult implements DetectiveMoveResult {

        /**
         * The move that was made.
         */
        protected final DetectiveMove.SearchClues move;

        /**
         * The answers for each vertex that was searched in the move.
         */
        protected final Map<String, Answer> clueAnswers;

        /**
         * Initializes fields. Makes {@link #clueAnswers} unmodifiable.
         * 
         * @param move
         *            The move that was made.
         * @param clueAnswers
         *            The answers for each vertex that was searched in the move.
         */
        public SearchCluesResult(final DetectiveMove.SearchClues move, final Map<String, Answer> clueAnswers) {
            this.move = move;
            this.clueAnswers = Collections.unmodifiableMap(clueAnswers);
        }

        /**
         * Returns The move that was made.
         * 
         * @return The move that was made.
         */
        public DetectiveMove.SearchClues getMove() {
            return this.move;
        }

        /**
         * Returns The answers for each vertex that was searched in the move.
         * 
         * @return The answers for each vertex that was searched in the move.
         */
        public Map<String, Answer> getClueAnswers() {
            return clueAnswers;
        }

    }

    /**
     * Models the result of attempting an arrest.
     * 
     * @author Anwar Reddick
     *
     */
    class AttemptArrestResult implements DetectiveMoveResult {

        /**
         * The move that was made.
         */
        protected final DetectiveMove.AttemptArrest move;

        /**
         * True if the arrest was successful, false otherwise.
         */
        protected final boolean answer;

        /**
         * Initializes fields.
         * 
         * @param move
         *            The move that was made.
         * @param answer
         *            True if the arrest was successful, false otherwise.
         */
        public AttemptArrestResult(final DetectiveMove.AttemptArrest move, final boolean answer) {
            this.move = move;
            this.answer = answer;
        }

        /**
         * Returns The move that was made.
         * 
         * @return The move that was made.
         */
        public DetectiveMove.AttemptArrest getMove() {
            return this.move;
        }

        /**
         * Returns True if the arrest was successful, false otherwise.
         * 
         * @return True if the arrest was successful, false otherwise.
         */
        public boolean getAnswer() {
            return answer;
        }

    }
}
