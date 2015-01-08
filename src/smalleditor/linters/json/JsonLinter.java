package smalleditor.linters.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeObject;
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
public class JsonLinter extends CommonLinter {
	private static final String DEFAULT_LINTER = "res/linter/jsonlint.js";

	
	protected boolean checkCode(Context context, String code) {
		Boolean superReturn = super.checkCode(context, code);
		if (superReturn) {
			Object[] args = new Object[] { code };
			ScriptableObject scope = context.initStandardObjects();
			try {
				linter.call(context, scope, null, args);
			} catch (JavaScriptException exception) {
				//invalid json
				superReturn = false;
			} catch (RhinoException exception) {
				String message = "JavaScript exception caused by Linter: "
						+ exception.getMessage();
				throw new RuntimeException(message, exception);
			}
		}
		return superReturn;
	}
	
	@Override
	protected Function findLinterFunction(ScriptableObject scope)
			throws IllegalArgumentException {
		Object object;
		if (ScriptableObject.hasProperty(scope, "jsonlint")) {
			object =  scope.get("jsonlint", scope);
			object = NativeObject.getProperty((NativeObject) object, "parse");
		} else {
			throw new IllegalArgumentException(
					"JSONLINT function missing in input");
		}

		if (!(object instanceof Function)) {
			throw new IllegalArgumentException(
					"JSONLINT is not a function");
		}
		return (Function) object;
	}
	
	protected BufferedReader getLinterReader()
			throws UnsupportedEncodingException {
		ClassLoader classLoader = JsonLinter.class.getClassLoader();
		
		InputStream inputStream = classLoader
				.getResourceAsStream(DEFAULT_LINTER);
		
		return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	}

	public void main(String[] args) {
		JsonLinterRunner runner = new JsonLinterRunner();
		runner.run(args);
	}

}
