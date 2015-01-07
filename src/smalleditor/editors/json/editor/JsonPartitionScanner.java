package smalleditor.editors.json.editor;

import java.util.ArrayList;
import java.util.List;
//import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.*;

import smalleditor.editors.common.editor.CommonPredicateWordRule;
import smalleditor.editors.common.editor.CommonWordDetector;

public class JsonPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JSON_DEFAULT = "__json_default";
	public final static String JSON_COMMENT = "__json_comment";
	public final static String JSON_KEYWORD = "__json_keyword";
	public final static String JSON_STRING = "__json_string";
	public final static String JSON_KEYSTRING = "__json_keystring";

	public final static IToken TOKEN_STRING = new Token(JSON_STRING);
	public final static IToken TOKEN_KEYSTRING = new Token(JSON_KEYSTRING);
	public final static IToken TOKEN_COMMENT = new Token(JSON_COMMENT);
	public final static IToken TOKEN_DEFAULT = new Token(JSON_DEFAULT);
	public final static IToken TOKEN_KEYWORD = new Token(JSON_KEYWORD);

	/**
	 * Array of keyword token strings.
	 */
	private static String[] keywordTokens = {
		"false", "null", "true", "undefined"
	};

	/**
	 * Creates a new JSPartitionScanner object.
	 */
	public JsonPartitionScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule("/*", "*/", TOKEN_COMMENT));
		
		rules.add(new SingleLineRule(":\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule(": \"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule(":	\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule(",\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule(", \"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule(",	\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule("[\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule("[ \"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule("[	\"", "\"", TOKEN_STRING, '\\'));
		
		rules.add(new SingleLineRule("\"", "\"", TOKEN_KEYSTRING, '\\'));
		
		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
				new CommonWordDetector(), TOKEN_DEFAULT, keywordTokens, TOKEN_KEYWORD);
		rules.add(keywordRule);

		setRuleList(rules);
	}

	private void setRuleList(List rules) {
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}