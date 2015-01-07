package smalleditor.linters.javascript.problem;

/**
 * Implementations of this class are used to handle problems returned from linter
 */
public interface ProblemHandler {

	/**
	 * Handles a problem occurred during the code check.
	 * 
	 * @param problem
	 *            the problem
	 */
	void handleProblem(Problem problem);

}
