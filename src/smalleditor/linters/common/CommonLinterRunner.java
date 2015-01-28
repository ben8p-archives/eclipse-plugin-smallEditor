package smalleditor.linters.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import smalleditor.linters.common.problem.IProblem;
import smalleditor.linters.common.problem.IProblemHandler;

public class CommonLinterRunner {

	private static final String PARAM_CHARSET = "--charset";
	private static final String PARAM_CUSTOM_LINTER = "--custom";
	private List<File> files;
	private Charset charset;
	protected File library;
	protected CommonLinter linter;

	public void run(String... args) {
		try {
			readArgs(args);
			ensureCharset();
			ensureInputFiles();
			loadLinter();
			//configureLinter();
			processFiles();
		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			System.out.println();
//			System.out
//					.println("Usage: JSLint [ <options> ] <input-file> [ <input-file> ... ]");
//			System.out.println("Options: --custom <custom-linter-file>");
//			System.out.println("         --charset <charset>");
		}
	}

	private void readArgs(String[] args) {
		files = new ArrayList<File>();
		String lastArg = null;
		for (String arg : args) {
			if (PARAM_CHARSET.equals(lastArg)) {
				setCharset(arg);
			} else if (PARAM_CUSTOM_LINTER.equals(lastArg)) {
				setLibrary(arg);
			} else if (PARAM_CHARSET.equals(arg)
					|| PARAM_CUSTOM_LINTER.equals(arg)) {
				// continue
			} else {
				File file = new File(arg);
				checkFile(file);
				files.add(file);
			}
			lastArg = arg;
		}
	}

	private void checkFile(File file) throws IllegalArgumentException {
		if (!file.isFile()) {
			throw new IllegalArgumentException("No such file: "
					+ file.getAbsolutePath());
		}
		if (!file.canRead()) {
			throw new IllegalArgumentException("Cannot read file: "
					+ file.getAbsolutePath());
		}
	}

	private void ensureCharset() {
		if (charset == null) {
			setCharset("UTF-8");
		}
	}

	private void setCharset(String name) {
		try {
			charset = Charset.forName(name);
		} catch (Exception exception) {
			throw new IllegalArgumentException(
					"Unknown or unsupported charset: " + name);
		}
	}

	private void setLibrary(String name) {
		library = new File(name);
	}

	private void ensureInputFiles() {
		if (files.isEmpty()) {
			throw new IllegalArgumentException("No input files");
		}
	}

	protected void loadLinter() {

	}

	private void processFiles() throws IOException {
		for (File file : files) {
			String code = readFileContents(file);
			IProblemHandler handler = new SysoutProblemHandler(
					file.getAbsolutePath());
			linter.check(code, handler);
		}
	}

//	private void configureLinter() {
//		linter.configure(new Configuration());
//	}

	private String readFileContents(File file) throws FileNotFoundException,
			IOException {
		FileInputStream inputStream = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, charset));
		try {
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				builder.append('\n');
				line = reader.readLine();
			}
			return builder.toString();
		} finally {
			reader.close();
		}
	}

	private static final class SysoutProblemHandler implements IProblemHandler {

		private final String fileName;

		public SysoutProblemHandler(String fileName) {
			this.fileName = fileName;
		}

		public void handleProblem(IProblem problem) {
			int line = problem.getLine();
			String message = problem.getMessage();
			System.out.println("Problem in file " + fileName + " at line "
					+ line + ": " + message);
		}

	}

}
