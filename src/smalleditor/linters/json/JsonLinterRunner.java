package smalleditor.linters.json;

import java.io.FileInputStream;

import smalleditor.linters.common.CommonLinterRunner;

public class JsonLinterRunner extends CommonLinterRunner {
	protected void loadLinter() {
		linter = new JsonLinter();
		try {
			if (library != null) {
				FileInputStream inputStream = new FileInputStream(library);
				try {
					linter.load(inputStream);
				} finally {
					inputStream.close();
				}
			} else {
				linter.load();
			}
		} catch (Exception exception) {
			String message = "Failed to load Linter library: "
					+ exception.getMessage();
			throw new IllegalArgumentException(message);
		}
	}
}
