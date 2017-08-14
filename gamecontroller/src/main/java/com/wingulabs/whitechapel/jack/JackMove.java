package com.wingulabs.whitechapel.jack;

import java.util.Collections;
import java.util.List;

/**
 * Models a move that Jack makes. This is also a container for nested
 * classes that actually model the moves.
 * @author anqtran
 *
 */
public interface JackMove {

	/**
	 * Moving two circles  and using 2 turns.
	 * @author anqtran
	 *
	 */
	class CoachMove implements JackMove {

		/**
		 * The path of circles the jack moved.
		 */
		private final List<String> path;

		/**
		 * Initializes fields. Makes them unmodifiable.
		 * @param path The path of squares the Jack moved using coach.
		 */
		public CoachMove(final List<String> path) {
			this.path = Collections.unmodifiableList(path);
		}

		/**
		 * Return The path of squares the Jack moved using coach.
		 * @return The path of squares the Jack moved using coach.
		 */
		public List<String> getPath() {
			return path;
		}
	}

	/**
	 * An alley move which lets Jack to move to any circle in the alley.
	 * @author anqtran
	 *
	 */
	class AlleyMove implements JackMove {

		/**
		 * The circle that Jack moved to.
		 */
		private final String targetCircle;

		/**
		 * Initializes fields. Makes them unmodifiable.
		 * @param targetCircle the circle that Jack moved to.
		 * for clues.
		 */
		public AlleyMove(final String targetCircle) {


			this.targetCircle = targetCircle;
		}


		/**
		 * Returns The circle that Jack moved to.
		 * @return The circle that Jack moved to.
		 */
		public String getTargetCircle() {
			return targetCircle;
		}

	}

	/**
	 * Jack arrive at his hideout.
	 * @author anqtran
	 *
	 */
	class HideOutArrive implements JackMove {
		/**
		 * Jack reaches his hideout.
		 */
		public HideOutArrive() {

		}

	}
}
