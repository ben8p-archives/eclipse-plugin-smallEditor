package smalleditor.linters.common.problem;

/**
 * Implementations of this class are used to handle problems returned from linter
 */
public interface IProblemHandler {

	/**
	 * Handles a problem occurred during the code check.
	 * 
	 * @param problem
	 *            the problem
	 */
	void handleProblem(IProblem problem);

}
