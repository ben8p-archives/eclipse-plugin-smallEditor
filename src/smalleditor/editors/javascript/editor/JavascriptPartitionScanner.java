package smalleditor.editors.javascript.editor;

import java.util.ArrayList;
import java.util.List;
//import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.*;

import smalleditor.editors.common.editor.CommonPredicateWordRule;
import smalleditor.editors.common.editor.CommonWordDetector;

/**
 * 
 * 
 * @author $Author: rdclark $, $Date: 2004/04/05 20:50:48 $
 * 
 * @version $Revision: 1.4 $
 */
public class JavascriptPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JS_DEFAULT = "__js_default";
	public final static String JS_COMMENT = "__js_comment";
	public final static String JS_KEYWORD = "__js_keyword";
	public final static String JS_STRING = "__js_string";

	public final static IToken TOKEN_STRING = new Token(JS_STRING);
	public final static IToken TOKEN_COMMENT = new Token(JS_COMMENT);
	public final static IToken TOKEN_DEFAULT = new Token(JS_DEFAULT);
	public final static IToken TOKEN_KEYWORD = new Token(JS_KEYWORD);

	/**
	 * Array of keyword token strings.
	 */
	private static String[] keywordTokens = {
		"break",
		"case",
		"class",
		"catch",
		"const",
		"continue",
		"debugger",
		"default",
		"delete",
		"do",
		"else",
		"export",
		"extends",
		"finally",
		"for",
		"function",
		"if",
		"import",
		"in",
		"instanceof",
		"let",
		"new",
		"return",
		"super",
		"switch",
		"this",
		"throw",
		"try",
		"typeof",
		"var",
		"void",
		"while",
		"with",
		"yield",
		"enum",
		"await",
		"implements",
		"package",
		"protected",
		"static",
		"interface",
		"private",
		"public",
		"abstract",
		"boolean",
		"byte",
		"char",
		"double",
		"final",
		"float",
		"goto",
		"int",
		"long",
		"native",
		"short",
		"synchronized",
		"transient",
		"volatile"
	
	};

	/**
	 * Array of constant token strings.
	 */
	private static String[] constantTokens = { "false", "null", "true", "undefined"};

	/**
	 * Creates a new JSPartitionScanner object.
	 */
	public JavascriptPartitionScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule("/*", "*/", TOKEN_COMMENT));
		rules.add(new EndOfLineRule("//", TOKEN_COMMENT));
		rules.add(new SingleLineRule("\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule("'", "'", TOKEN_STRING, '\\'));
		
		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
				new CommonWordDetector(), TOKEN_DEFAULT, keywordTokens, TOKEN_KEYWORD);
		
		keywordRule.addWords(constantTokens, TOKEN_KEYWORD);
		rules.add(keywordRule);

		setRuleList(rules);
	}

	private void setRuleList(List rules) {
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}