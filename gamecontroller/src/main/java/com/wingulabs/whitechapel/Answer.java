package com.wingulabs.whitechapel;

/**
 * Potential answers to a detective question.
 * @author Anwar Reddick
 *
 */
public enum Answer {

	/**
	 * A Yes answer.
	 */
	YES,

	/**
	 * A No answer.
	 */
	NO,

	/**
	 * None means that the Jack never got to answering the question. This
	 * happens when searching for clues and Jack answers Yes before asking all
	 * potential questions.
	 */
	NONE
}
