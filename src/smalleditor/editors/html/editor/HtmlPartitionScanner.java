package smalleditor.editors.html.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import smalleditor.editors.common.editor.CommonPredicateWordRule;
import smalleditor.editors.common.editor.CommonWordDetector;

public class HtmlPartitionScanner extends RuleBasedPartitionScanner {
	public final static String HTML_DEFAULT = "__html_default";
	public final static String HTML_COMMENT = "__html_comment";
	public final static String HTML_TAG = "__html_tag";
	public final static String HTML_STRING = "__html_string";
	public final static String HTML_ATTRIBUTE = "__html_attribute";

	public final static IToken TOKEN_STRING = new Token(HTML_STRING);
	public final static IToken TOKEN_TAG = new Token(HTML_TAG);
	public final static IToken TOKEN_COMMENT = new Token(HTML_COMMENT);
	public final static IToken TOKEN_DEFAULT = new Token(HTML_DEFAULT);
	public final static IToken TOKEN_ATTRIBUTE = new Token(HTML_ATTRIBUTE);


	public HtmlPartitionScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule("<!--", "-->", TOKEN_COMMENT));
		rules.add(new SingleLineRule("\"", "\"", TOKEN_STRING));
		rules.add(new SingleLineRule(">", "<", TOKEN_STRING));
		//rules.add(new SingleLineRule(" ", "=", TOKEN_ATTRIBUTE));
		//rules.add(new SingleLineRule("\t", "=", TOKEN_ATTRIBUTE));
		
//		rules.add(new WordPatternRule(new HtmlWordDetector(), "<", ">", TOKEN_TAG));
//		rules.add(new WordPatternRule(new HtmlWordDetector(), "<", " ", TOKEN_TAG));
//		rules.add(new WordPatternRule(new HtmlWordDetector(), "<", "	", TOKEN_TAG));
//		
//		rules.add(new XmlTagRule(new HtmlWordDetector(), ">", TOKEN_TAG));
//		rules.add(new XmlTagRule(new HtmlWordDetector(), " ", TOKEN_TAG));
//		rules.add(new XmlTagRule(new HtmlWordDetector(), "	", TOKEN_TAG));
//		rules.add(new XmlTagRule(new HtmlWordDetector(), "\n", TOKEN_TAG));

		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
				new CommonWordDetector(), TOKEN_DEFAULT, new String[] {}, TOKEN_DEFAULT);
		rules.add(keywordRule);
		
		setRuleList(rules);
	}

	private void setRuleList(List rules) {
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}