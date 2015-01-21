package smalleditor.editors.css.editor;

import java.util.Vector;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;


public class CssCommentScanner extends RuleBasedScanner {
	/**
	 * Creates a new JSFuncScanner object.
	 * 
	 * @param manager
	 */
	public CssCommentScanner(IToken iToken) {
		Vector rules = new Vector();

		// Add rule for single and double quotes
		rules.add(new MultiLineRule("/*", "*/", iToken));
		rules.add(new EndOfLineRule("//", iToken));

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