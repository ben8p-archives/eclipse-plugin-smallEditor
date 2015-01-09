package smalleditor.editors.css.editor;

import org.eclipse.jface.text.*;
import java.util.*;
import org.eclipse.jface.text.rules.*;

import org.eclipse.swt.graphics.Color;

import smalleditor.editors.common.editor.CommonWhitespaceDetector;

/**
 * 
 * 
 * @author $Author: agfitzp $, $Date: 2003/05/28 15:17:11 $
 * 
 * @version $Revision: 1.1 $
 */
public class CssStringScanner extends RuleBasedScanner {
	/**
	 * Creates a new JSFuncScanner object.
	 * 
	 * @param manager
	 */
	public CssStringScanner(Color aColor) {
		IToken string = new Token(new TextAttribute(aColor));
		Vector rules = new Vector();

		// Add rule for single and double quotes
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new CommonWhitespaceDetector()));

		IRule[] result = new IRule[rules.size()];
		rules.copyInto(result);
		setRules(result);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public IToken nextToken() {
		return super.nextToken();
	}
}