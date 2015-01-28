package smalleditor.linters.common.problem;

/**
 * Holds information about a problem found by JSHint.
 */
public interface IProblem {

	/**
	 * Returns the line number in which the problem occurred.
	 * 
	 * @return the line number, beginning with 1
	 */
	int getLine();

	/**
	 * Returns the character offset within the line in which the character
	 * occurred.
	 * 
	 * @return the character offset, beginning with 0
	 */
	int getCharacter();

	/**
	 * The problem message returned from JSHint.
	 * 
	 * @return the message
	 */
	String getMessage();

	String getId();

}
