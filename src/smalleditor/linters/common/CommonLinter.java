package smalleditor.linters.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptableObject;

import smalleditor.linters.common.problem.IProblem;
import smalleditor.linters.common.problem.IProblemHandler;
import smalleditor.linters.common.problem.Problem;
import smalleditor.linters.common.text.Text;
/**
 * Lightweight Java wrapper for the Linter code analysis tool.
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * Linter linter = new Linter();
 * linter.load();
 * linter.configure( new Configuration() );
 * linter.check( jsCode, new ProblemHandler() { ... } );
 * </pre>
 * 
 * @see http://www.linter.com/
 */
public class CommonLinter {
	protected Function linter;
	private Function fakeConsole;
	private static final int DEFAULT_LINTER_INDENT = 4;
	private int indent = DEFAULT_LINTER_INDENT;
	
	/**
	 * Loads the default Linter library.
	 * 
	 * @see #getDefaultLibraryVersion()
	 */
	public void load() throws IOException {
		Reader reader = getLinterReader();
		try {
			load(reader);
		} finally {
			reader.close();
		}
	}

	/**
	 * Loads a custom Linter library. The input stream must provide the contents
	 * of the file <code>linter.js</code> found in the Linter distribution.
	 * <p>
	 * JSLint is also supported. In this case the file to provide is
	 * <code>jslint.js</code>.
	 * </p>
	 * 
	 * @param inputStream
	 *            an input stream to load the the Linter library from
	 * @throws IOException
	 *             if an I/O error occurs while reading from the input stream
	 * @throws IllegalArgumentException
	 *             if the given input is not a proper Linter library file
	 */
	public void load(InputStream inputStream) throws IOException {
		Reader reader = new InputStreamReader(inputStream);
		try {
			load(reader);
		} finally {
			reader.close();
		}
	}

	/**
	 * Checks the given JavaScript code. All problems will be reported to the
	 * given problem handler.
	 * 
	 * @param code
	 *            the JavaScript code to check, must not be null
	 * @param handler
	 *            the handler to report problems to or <code>null</code>
	 * @return <code>true</code> if no problems have been found, otherwise
	 *         <code>false</code>
	 */
	public boolean check(String code, IProblemHandler handler) {
		if (code == null) {
			throw new NullPointerException("code is null");
		}
		return check(new Text(code), handler);
	}

	public boolean check(Text text, IProblemHandler handler) {
		if (text == null) {
			throw new NullPointerException("code is null");
		}
		if (linter == null) {
			throw new IllegalStateException("Linter is not loaded");
		}
		boolean result = true;
		String code = text.getContent();
		// Don't feed linter with empty strings,
		// However, consider an empty string valid
		if (code.trim().length() != 0) {
			Context context = Context.enter();
			try {
				result = checkCode(context, code);
				if (!result && handler != null) {
					handleProblems(handler, text);
				}
			} finally {
				Context.exit();
			}
		}
		return result;
	}

	private void load(Reader reader) throws IOException {
		Context context = Context.enter();
		try {
			ScriptableObject scope = context.initStandardObjects();
			
			String code = IOUtils.toString(reader);
			//System.out.println(code);
			context.evaluateString(scope, code, "linter library", 1, null);
			
//			evaluatedCode = context.evaluateReader(scope, reader, "linter library", 1, null);
//			System.out.println(Context.toString(evaluatedCode));
			
			code = "fakeConsole = function(){ console = {log:function(){},error:function(){},trace:function(){}}; };";
			context.evaluateString(scope, code, "fake console", 1, null);
			//System.out.println(Context.toString(evaluatedCode));
			
			fakeConsole = (Function) scope.get("fakeConsole", scope);
			
			linter = findLinterFunction(scope);
		} catch (RhinoException exception) {
			throw new IllegalArgumentException(
					"Could not evaluate JavaScript input", exception);
		} catch (IOException exception) {
			throw new IllegalArgumentException(
					"Could not evaluate JavaScript input", exception);
		} finally {
			Context.exit();
		}
	}

	protected boolean checkCode(Context context, String code) {
		try {
			ScriptableObject scope = context.initStandardObjects();
			fakeConsole.call(context, scope, null, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	protected Function findLinterFunction(ScriptableObject scope)
			throws IllegalArgumentException {
		
			throw new IllegalArgumentException("No linter found");
	}

	private void handleProblems(IProblemHandler handler, Text text) {
		NativeArray errors = (NativeArray) linter.get("errors", linter);
		long length = errors.getLength();
		//int lengthE = exclude.length;
		//Boolean exit = false;
		for (int i = 0; i < length; i++) {
			//exit = false;
			Object object = errors.get(i, errors);
			ScriptableObject error = (ScriptableObject) object;
			if (error != null) {
				//String reason = getPropertyAsString(error, "reason", "");
				/*for (int j = 0; j < lengthE; j++) {
					if (reason.matches(exclude[j])) {
						exit = true;
						break;
					}
				}*/
				//if (exit == false) {
				IProblem problem = createProblem(error, text);
				handler.handleProblem(problem);
				//}
			}
		}
	}

	private Problem createProblem(ScriptableObject error, Text text) {
		
		String reason = getPropertyAsString(error, "reason", "");
		int line = getPropertyAsInt(error, "line", -1);
		int character = getPropertyAsInt(error, "character", -1);
		String id = getPropertyAsString(error, "id", "(error)");
		if (character > 0) {
			character = fixPosition(text, line, character);
		}
		
		String message = reason.endsWith(".") ? reason.substring(0,
				reason.length() - 1) : reason;
		return new Problem(line, character, message, id);
	}

	private int fixPosition(Text text, int line, int character) {
		// Linter reports physical character positions instead of a character
		// index,
		// i.e. every tab character is multiplied with the indent.
		String string = text.getContent();
		int offset = text.getLineOffset(line - 1);
		int indentIndex = 0;
		int charIndex = 0;
		int maxIndex = Math.min(character, string.length() - offset) - 1;
		while (indentIndex < maxIndex) {
			boolean isTab = string.charAt(offset + indentIndex) == '\t';
			indentIndex += isTab ? indent : 1;
			charIndex++;
		}
		return charIndex;
	}

	private static String getPropertyAsString(ScriptableObject object,
			String name, String defaultValue) {
		String result = defaultValue;
		Object property = ScriptableObject.getProperty(object, name);
		if (property instanceof String) {
			result = (String) property;
		} else {
			result = property.toString();
		}
		return result;
	}

	private static int getPropertyAsInt(ScriptableObject object, String name,
			int defaultValue) {
		int result = defaultValue;
		Object property = ScriptableObject.getProperty(object, name);
		if (property instanceof Number) {
			result = ((Number) property).intValue();
		}
		return result;
	}

	protected BufferedReader getLinterReader()
			throws UnsupportedEncodingException {
//		ClassLoader classLoader = JsonLinter.class.getClassLoader();
//		
//		InputStream inputStream = classLoader
//				.getResourceAsStream(DEFAULT_LINTER);
//		
//		return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		return null;
	}

	public void main(String[] args) {
//		JsonLinterRunner runner = new JsonLinterRunner();
//		runner.run(args);
	}

}
