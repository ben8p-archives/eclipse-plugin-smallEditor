package smalleditor.linters.common.problem;

public class Problem implements IProblem {
	private final int line;
	private final int character;
	private final String message;
	private final String id;

	public Problem(int line, int character, String message, String id) {
		this.line = line;
		this.character = character;
		this.message = message;
		this.id = id;
	}

	public int getLine() {
		return line;
	}

	public int getCharacter() {
		return character;
	}

	public String getMessage() {
		return message;
	}

	public String getId() {
		return id;
	}

}
