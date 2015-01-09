/*
 * Created on May 16, 2003
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSSyntaxScanner.java,v $
 * Revision 1.2  2003/05/30 20:53:09  agfitzp
 * 0.0.2 : Outlining is now done as the user types. Some other bug fixes.
 *
 *========================================================================
 */
package smalleditor.editors.css.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner for detecting syntactic elements: comments, strings, classes, functions
 *
 * @author fitzpata
 *
 */
public class CssOutlineScanner extends BufferedRuleBasedScanner {
	public final static String CSS_CLASSNAME = "__css_classname";

	public final static IToken TOKEN_CLASSNAME = new Token(CSS_CLASSNAME);

	/**
	 * Creates a new JSSyntaxScanner object.
	 */
	public CssOutlineScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule(".", "{", TOKEN_CLASSNAME));
		rules.add(new MultiLineRule(".", ",", TOKEN_CLASSNAME));
		
		setRuleList(rules);
	}


	/**
	 * set the rule list
	 * @param rules
	 */
	private void setRuleList(List rules)
	{
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}