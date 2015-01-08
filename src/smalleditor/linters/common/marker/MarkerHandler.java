package smalleditor.linters.common.marker;

import org.eclipse.core.runtime.CoreException;

import smalleditor.linters.common.problem.Problem;
import smalleditor.linters.common.problem.ProblemHandler;
import smalleditor.linters.common.text.Text;

public final class MarkerHandler implements ProblemHandler {

	private final MarkerAdapter markerAdapter;
	private final Text code;

	public MarkerHandler(MarkerAdapter markerAdapter, Text code) {
		this.markerAdapter = markerAdapter;
		this.code = code;
	}

	public void handleProblem(Problem problem) {
		int line = problem.getLine();
		int character = problem.getCharacter();
		String message = problem.getMessage();
		String type = problem.getMessage().contains("Stopping.") 
				? "ERROR"
				: "WARNING";
		/*problem.getId().equals("(error)") 
				? "ERROR"
				: problem.getId().equals("(warning)") 
					? "WARNING"
		*/ 
					
		if (isValidLine(line)) {
			int start = -1;
			if (isValidCharacter(line, character)) {
				start = code.getLineOffset(line - 1) + character;
			}
			createMarker(line, start, message, type);
		} else {
			createMarker(-1, -1, message, type);
		}
	}

	private void createMarker(int line, int start, String message,
			String codeStr) {
		try {
			markerAdapter.createMarker(line, start, start, message, codeStr);
		} catch (CoreException ce) {

		}
	}

	private boolean isValidLine(int line) {
		return line >= 1 && line <= code.getLineCount();
	}

	private boolean isValidCharacter(int line, int character) {
		return character >= 0 && character <= code.getLineLength(line - 1);
	}

}
