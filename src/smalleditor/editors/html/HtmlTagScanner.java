/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.editors.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
//import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import smalleditor.common.rules.AttributeNameWordDetector;
import smalleditor.common.rules.BreakingMultiLineRule;
import smalleditor.common.rules.BrokenStringRule;
import smalleditor.common.rules.CharacterMapRule;
import smalleditor.common.rules.ExtendedWordRule;
import smalleditor.common.rules.MultiCharacterRule;
import smalleditor.common.rules.QueuedRuleBasedScanner;
import smalleditor.common.rules.TagNameWordDetector;
import smalleditor.common.rules.TagWordRule;
import smalleditor.editors.common.CommonWhitespaceDetector;
import smalleditor.preferences.ColorManager;
import smalleditor.preferences.IPreferenceNames;

/**
 * @author Max Stepanov
 */
public class HtmlTagScanner extends QueuedRuleBasedScanner {
	private ColorManager colorManager = ColorManager.getDefault();
	
	// as per the html5 spec, these are elements that define "sections", but
	// we've added
	// the <html> tag itself to the list.
	// see http://dev.w3.org/html5/spec/Overview.html#sections
	@SuppressWarnings("nls")
	private static final String[] STRUCTURE_DOT_ANY = { "html", "head", "body",
			"header", "address", "nav", "section", "article", "footer",
			"aside", "hgroup", "h1", "h2", "h3", "h4", "h5", "h6" };

	@SuppressWarnings("nls")
	private static final String[] BLOCK_DOT_ANY = { "blockquote", "dd", "div",
			"dl", "dt", "fieldset", "form", "frame", "frameset", "iframe",
			"noframes", "object", "ol", "p", "ul", "applet", "center", "dir",
			"hr", "menu", "pre" };

	@SuppressWarnings("nls")
	private static final String[] TAG_INLINE_ANY = { "a", "abbr", "acronym",
			"area", "b", "base", "basefont", "bdo", "big", "br", "button",
			"caption", "cite", "code", "col", "colgroup", "del", "dfn", "em",
			"font", "i", "img", "input", "ins", "isindex", "kbd", "label",
			"legend", "li", "link", "map", "meta", "noscript", "optgroup",
			"option", "param", "q", "s", "samp", "script", "select", "small",
			"span", "strike", "strong", "style", "sub", "sup", "table",
			"tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr",
			"tt", "u", "var", "canvas", "audio", "video" };

	@SuppressWarnings("nls")
	private static final String[] QUOTED_STRING_BREAKS = { "/>", ">" };

	private final IToken doubleQuotedStringToken = createToken(HtmlTokenType.DOUBLE_QUOTED_STRING);
	private final IToken singleQuotedStringToken = createToken(HtmlTokenType.SINGLE_QUOTED_STRING);
	private final IToken attributeStyleToken = createToken(HtmlTokenType.ATTR_STYLE);
	// private final IToken attributeScriptToken =
	// createToken(HtmlTokenType.ATTR_SCRIPT);
	private final IToken equalToken = createToken(HtmlTokenType.EQUAL);

	// private ITokenScanner cssTokenScanner = new CSSCodeScannerFlex();
	// private ITokenScanner jsTokenScanner = new JSCodeScanner();

	private Stack<IToken> tokenHistory = new Stack<IToken>();
	// private String tagName;
	private boolean hasTokens;
	private boolean rescanNestedLanguages;

	/**
	 * HTMLTagScanner
	 */
	public HtmlTagScanner() {
		this(true);
	}

	/**
	 * HTMLTagScanner
	 * 
	 * @param rescanNestedLanguges
	 *            A flag indicating if nested languages in attributes should be
	 *            rescanned in their source language. When this is set to false,
	 *            the entire attribute value is treated as a single lexeme
	 */
	public HtmlTagScanner(boolean rescanNestedLanguges) {
		this.rescanNestedLanguages = rescanNestedLanguges;

		List<IRule> rules = new ArrayList<IRule>();

		// Add rule for double quotes
		rules.add(new MultiLineRule("\"", "\"", doubleQuotedStringToken, '\\')); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new BreakingMultiLineRule(
				"\"", "\"", QUOTED_STRING_BREAKS, doubleQuotedStringToken, '\\')); //$NON-NLS-1$ //$NON-NLS-2$

		// Add a rule for single quotes
		rules.add(new MultiLineRule("'", "'", singleQuotedStringToken, '\\')); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new BreakingMultiLineRule(
				"'", "'", QUOTED_STRING_BREAKS, singleQuotedStringToken, '\\')); //$NON-NLS-1$ //$NON-NLS-2$

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new CommonWhitespaceDetector()));

		// Tags
		WordRule tagWordRule = new TagWordRule(new TagNameWordDetector(),
				createToken(HtmlTokenType.META), true) {
			@Override
			protected IToken getWordToken(String word) {
				// tagName = word;
				return null;
			}
		};
		tagWordRule.addWord("script", createToken(HtmlTokenType.SCRIPT)); //$NON-NLS-1$
		tagWordRule.addWord("style", createToken(HtmlTokenType.STYLE)); //$NON-NLS-1$
		IToken structureDotAnyToken = createToken(HtmlTokenType.STRUCTURE_TAG);
		for (String tag : STRUCTURE_DOT_ANY) {
			tagWordRule.addWord(tag, structureDotAnyToken);
		}
		IToken blockDotAnyToken = createToken(HtmlTokenType.BLOCK_TAG);
		for (String tag : BLOCK_DOT_ANY) {
			tagWordRule.addWord(tag, blockDotAnyToken);
		}
		IToken inlineAnyToken = createToken(HtmlTokenType.INLINE_TAG);
		for (String tag : TAG_INLINE_ANY) {
			tagWordRule.addWord(tag, inlineAnyToken);
		}
		rules.add(tagWordRule);

		WordRule attributeWordRule = new ExtendedWordRule(
				new AttributeNameWordDetector(),
				createToken(HtmlTokenType.ATTRIBUTE), true) {
			@Override
			protected IToken getWordToken(String word) {
				// return HTMLUtils.isJSAttribute(tagName, word) ?
				// attributeScriptToken : null;
				return null;
			}

		};
		attributeWordRule.addWord("id", createToken(HtmlTokenType.ATTR_ID)); //$NON-NLS-1$
		attributeWordRule.addWord(
				"class", createToken(HtmlTokenType.ATTR_CLASS)); //$NON-NLS-1$
		attributeWordRule.addWord("style", attributeStyleToken); //$NON-NLS-1$
		rules.add(attributeWordRule);

		rules.add(new MultiCharacterRule(
				"</", createToken(HtmlTokenType.TAG_START))); //$NON-NLS-1$
		rules.add(new MultiCharacterRule(
				"/>", createToken(HtmlTokenType.TAG_SELF_CLOSE))); //$NON-NLS-1$

		CharacterMapRule charsRule = new CharacterMapRule();
		charsRule.add('<', createToken(HtmlTokenType.TAG_START));
		charsRule.add('>', createToken(HtmlTokenType.TAG_END));
		charsRule.add('=', equalToken);
		rules.add(charsRule);

		setRules(rules.toArray(new IRule[rules.size()]));
		setDefaultReturnToken(createToken(HtmlTokenType.TEXT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.text.rules.DelegatingRuleBasedScanner#setRange
	 * (org.eclipse.jface.text.IDocument, int, int)
	 */
	@Override
	public void setRange(IDocument document, int offset, int length) {
		super.setRange(document, offset, length);
		tokenHistory.clear();
		// tagName = null;
		hasTokens = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.text.rules.DelegatingRuleBasedScanner#nextToken
	 * ()
	 */
	@Override
	public IToken nextToken() {
		IToken token;
		if (!hasTokens) {
			hasTokens = true;
			token = findBrokenToken();
			if (!token.isUndefined()) {
				return token;
			}
		}
		token = super.nextToken();
		if (rescanNestedLanguages
				&& (doubleQuotedStringToken == token || singleQuotedStringToken == token)) {
			// IToken attributeToken = getAttributeToken();
			// ITokenScanner tokenScanner = null;
			// if (attributeScriptToken == attributeToken)
			// {
			// tokenScanner = jsTokenScanner;
			// }
			// else if (attributeStyleToken == attributeToken)
			// {
			// tokenScanner = cssTokenScanner;
			// }
			tokenHistory.clear();
			// int offset = getTokenOffset();
			// int length = getTokenLength() - 2;
			// if (tokenScanner != null && length > 0)
			// {
			// queueToken(token, offset, 1);
			// queueDelegate(tokenScanner, offset + 1, length);
			// queueToken(token, offset + length + 1, 1);
			// return super.nextToken();
			// }
		}
		if (!token.isWhitespace()) {
			tokenHistory.push(token);
		}
		return token;
	}

	private IToken findBrokenToken() {
		fTokenOffset = fOffset;
		fColumn = UNDEFINED;
		return new BrokenStringRule(singleQuotedStringToken,
				doubleQuotedStringToken).evaluate(this);
	}

	// private IToken getAttributeToken()
	// {
	// if (tokenHistory.size() < 2 || equalToken != tokenHistory.pop())
	// {
	// return null;
	// }
	// return tokenHistory.pop();
	// }

	/**
	 * createToken
	 * 
	 * @param type
	 * @return
	 */
	protected IToken createToken(HtmlTokenType type) {
		return createToken(type.getScope());
	}

	/**
	 * createToken
	 * 
	 * @param string
	 * @return
	 */
	protected IToken createToken(String string) {
		if(string == HtmlTokenType.DOUBLE_QUOTED_STRING.getScope() || string == HtmlTokenType.SINGLE_QUOTED_STRING.getScope()) {
			return new Token(new TextAttribute(
					colorManager.getColorFromPreferencesKey(IPreferenceNames.P_STRING_COLOR)));
		}
		if(string == HtmlTokenType.ATTR_STYLE.getScope() || string == HtmlTokenType.ATTRIBUTE.getScope() || string == HtmlTokenType.ATTR_CLASS.getScope() || string == HtmlTokenType.ATTR_ID.getScope()) {
			return new Token(new TextAttribute(
					colorManager.getColorFromPreferencesKey(IPreferenceNames.P_DEFAULT_COLOR), null, SWT.BOLD));
		}
		if(string == HtmlTokenType.META.getScope() || string == HtmlTokenType.SCRIPT.getScope() || string == HtmlTokenType.STYLE.getScope() || string == HtmlTokenType.BLOCK_TAG.getScope() || string == HtmlTokenType.STRUCTURE_TAG.getScope() || string == HtmlTokenType.INLINE_TAG.getScope() || string == HtmlTokenType.TAG_START.getScope() || string == HtmlTokenType.TAG_SELF_CLOSE.getScope() || string == HtmlTokenType.TAG_END.getScope()) {
			return new Token(new TextAttribute(
					colorManager.getColorFromPreferencesKey(IPreferenceNames.P_KEYWORD_COLOR)));
		}
		
		return new Token(string);
	}

}
