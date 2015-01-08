package smalleditor.linters.javascript;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptableObject;

import smalleditor.linters.common.CommonLinter;
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
public class JavascriptLinter extends CommonLinter {

	private static final String DEFAULT_LINTER = "res/linter/jslint.js";

	
	protected boolean checkCode(Context context, String code) {
		Boolean superReturn = super.checkCode(context, code);
		if (superReturn) {
			Object[] args = new Object[] { code };
			Object object = null;
			ScriptableObject scope = context.initStandardObjects();
			try {
				object = linter.call(context, scope, null, args);
			} catch (JavaScriptException exception) {
				String message = "JavaScript exception thrown by Linter: "
						+ exception.getMessage();
				throw new RuntimeException(message, exception);
			} catch (RhinoException exception) {
				String message = "JavaScript exception caused by Linter: "
						+ exception.getMessage();
				throw new RuntimeException(message, exception);
			}
			return object != null ? ((Boolean) object).booleanValue() : false;
		}
		return false;
	}
	
	@Override
	protected Function findLinterFunction(ScriptableObject scope)
			throws IllegalArgumentException {
		Object object;
		if (ScriptableObject.hasProperty(scope, "JSHINT")) {
			object = scope.get("JSHINT", scope);
		} else if (ScriptableObject.hasProperty(scope, "JSLINT")) {
			object = scope.get("JSLINT", scope);
		} else {
			throw new IllegalArgumentException(
					"Global LINTER or JSLINT function missing in input");
		}

		if (!(object instanceof Function)) {
			throw new IllegalArgumentException(
					"Global LINTER or JSLINT is not a function");
		}
		return (Function) object;
	}
	
	@Override
	protected BufferedReader getLinterReader()
			throws UnsupportedEncodingException {
		ClassLoader classLoader = JavascriptLinter.class.getClassLoader();
		
		InputStream inputStream = classLoader
				.getResourceAsStream(DEFAULT_LINTER);
		
		return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	}

	@Override
	public void main(String[] args) {
		JavascriptLinterRunner runner = new JavascriptLinterRunner();
		runner.run(args);
	}

}
