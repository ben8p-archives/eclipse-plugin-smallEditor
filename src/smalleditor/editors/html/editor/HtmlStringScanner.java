package smalleditor.editors.html.editor;

import org.eclipse.jface.text.*;
import java.util.*;
import org.eclipse.jface.text.rules.*;

import org.eclipse.swt.graphics.Color;

import smalleditor.editors.common.editor.CommonWhitespaceDetector;

public class HtmlStringScanner extends RuleBasedScanner {
	public HtmlStringScanner(Color aColor) {
		IToken string = new Token(new TextAttribute(aColor));
		Vector rules = new Vector();

		// Add rule for single and double quotes
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new CommonWhitespaceDetector()));

		IRule[] result = new IRule[rules.size()];
		rules.copyInto(result);
		setRules(result);
	}

	
	public IToken nextToken() {
		return super.nextToken();
	}
}