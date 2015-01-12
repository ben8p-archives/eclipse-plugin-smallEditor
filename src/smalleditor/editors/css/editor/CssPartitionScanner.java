package smalleditor.editors.css.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;

import smalleditor.editors.common.editor.CommonHexaColorDetector;
import smalleditor.editors.common.editor.CommonPredicateWordRule;


public class CssPartitionScanner extends RuleBasedPartitionScanner {
	public final static String CSS_DEFAULT = "__css_default";
	public final static String CSS_COMMENT = "__css_comment";
	public final static String CSS_KEYWORD = "__css_keyword";
	public final static String CSS_CLASSNAME = "__css_classname";
	public final static String CSS_STRING = "__css_string";
	public final static String CSS_COLOR = "__css_color";
	

	public final static IToken TOKEN_STRING = new Token(CSS_STRING);
	public final static IToken TOKEN_COMMENT = new Token(CSS_COMMENT);
	public final static IToken TOKEN_DEFAULT = new Token(CSS_DEFAULT);
	public final static IToken TOKEN_KEYWORD = new Token(CSS_KEYWORD);
	public final static IToken TOKEN_COLOR = new Token(CSS_COLOR);
	public final static IToken TOKEN_CLASSNAME = new Token(CSS_CLASSNAME);

	/**
	 * Array of keyword token strings.
	 * see : http://www.blooberry.com/indexdot/css/propindex/all.htm
	 */
	private static String[] keywordTokens = {
		"accelerator",
		"azimuth",
		"background",
		"background-attachment",
		"background-color",
		"background-image",
		"background-position",
		"background-position-x",
		"background-position-y",
		"background-repeat",
		"behavior",
		"border",
		"border-bottom",
		"border-bottom-color",
		"border-bottom-style",
		"border-bottom-width",
		"border-collapse",
		"border-color",
		"border-left",
		"border-left-color",
		"border-left-style",
		"border-left-width",
		"border-right",
		"border-right-color",
		"border-right-style",
		"border-right-width",
		"border-spacing",
		"border-style",
		"border-top",
		"border-top-color",
		"border-top-style",
		"border-top-width",
		"border-width",
		"bottom",
		"caption-side",
		"clear",
		"clip",
		"color",
		"content",
		"counter-increment",
		"counter-reset",
		"cue",
		"cue-after",
		"cue-before",
		"cursor",
		"direction",
		"display",
		"elevation",
		"empty-cells",
		"filter",
		"float",
		"font",
		"font-family",
		"font-size",
		"font-size-adjust",
		"font-stretch",
		"font-style",
		"font-variant",
		"font-weight",
		"height",
		"ime-mode",
		"include-source",
		"layer-background-color",
		"layer-background-image",
		"layout-flow",
		"layout-grid",
		"layout-grid-char",
		"layout-grid-char-spacing",
		"layout-grid-line",
		"layout-grid-mode",
		"layout-grid-type",
		"left",
		"letter-spacing",
		"line-break",
		"line-height",
		"list-style",
		"list-style-image",
		"list-style-position",
		"list-style-type",
		"margin",
		"margin-bottom",
		"margin-left",
		"margin-right",
		"margin-top",
		"marker-offset",
		"marks",
		"max-height",
		"max-width",
		"min-height",
		"min-width",
		"-moz-binding",
		"-moz-border-radius",
		"-moz-border-radius-topleft",
		"-moz-border-radius-topright",
		"-moz-border-radius-bottomright",
		"-moz-border-radius-bottomleft",
		"-moz-border-top-colors",
		"-moz-border-right-colors",
		"-moz-border-bottom-colors",
		"-moz-border-left-colors",
		"-moz-opacity",
		"-moz-outline",
		"-moz-outline-color",
		"-moz-outline-style",
		"-moz-outline-width",
		"-moz-user-focus",
		"-moz-user-input",
		"-moz-user-modify",
		"-moz-user-select",
		"orphans",
		"outline",
		"outline-color",
		"outline-style",
		"outline-width",
		"overflow",
		"overflow-X",
		"overflow-Y",
		"padding",
		"padding-bottom",
		"padding-left",
		"padding-right",
		"padding-top",
		"page",
		"page-break-after",
		"page-break-before",
		"page-break-inside",
		"pause",
		"pause-after",
		"pause-before",
		"pitch",
		"pitch-range",
		"play-during",
		"position",
		"quotes",
		"-replace",
		"richness",
		"right",
		"ruby-align",
		"ruby-overhang",
		"ruby-position",
		"-set-link-source",
		"size",
		"speak",
		"speak-header",
		"speak-numeral",
		"speak-punctuation",
		"speech-rate",
		"stress",
		"scrollbar-arrow-color",
		"scrollbar-base-color",
		"scrollbar-dark-shadow-color",
		"scrollbar-face-color",
		"scrollbar-highlight-color",
		"scrollbar-shadow-color",
		"scrollbar-3d-light-color",
		"scrollbar-track-color",
		"table-layout",
		"text-align",
		"text-align-last",
		"text-decoration",
		"text-indent",
		"text-justify",
		"text-overflow",
		"text-shadow",
		"text-transform",
		"text-autospace",
		"text-kashida-space",
		"text-underline-position",
		"top",
		"touch-callout",
		"unicode-bidi",
		"user-select",
		"-use-link-source",
		"vertical-align",
		"visibility",
		"voice-family",
		"volume",
		"white-space",
		"widows",
		"width",
		"word-break",
		"word-spacing",
		"word-wrap",
		"writing-mode",
		"z-index",
		"zoom"
	};

	/**
	 * Creates a new JSPartitionScanner object.
	 */
	public CssPartitionScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule("/*", "*/", TOKEN_COMMENT));
		rules.add(new MultiLineRule(".", "{", TOKEN_CLASSNAME));
		rules.add(new MultiLineRule(".", ",", TOKEN_CLASSNAME));
		rules.add(new MultiLineRule(":", "", TOKEN_STRING));
		
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "0", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "1", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "2", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "3", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "4", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "5", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "6", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "7", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "8", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "9", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "a", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "b", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "c", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "d", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "e", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "f", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "A", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "B", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "C", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "D", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "E", TOKEN_COLOR));
		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "F", TOKEN_COLOR));
		
		List<String> allKeywords = new ArrayList<String>();
		for (String keywordToken : keywordTokens) {
			allKeywords.add(keywordToken);
			allKeywords.add("-webkit-" + keywordToken);
			allKeywords.add("-moz-" + keywordToken);
			allKeywords.add("-opera-" + keywordToken);
			allKeywords.add("-ms-" + keywordToken);
			allKeywords.add("-khtml-" + keywordToken);
		}
		String[] simpleArray = new String[ allKeywords.size() ];
		allKeywords.toArray( simpleArray );
		
		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
				new CssWordDetector(), TOKEN_DEFAULT, simpleArray, TOKEN_KEYWORD);
		rules.add(keywordRule);


		setRuleList(rules);
	}

	private void setRuleList(List rules) {
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}