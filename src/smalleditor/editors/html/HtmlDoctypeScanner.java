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
import java.util.regex.Pattern;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import smalleditor.common.rules.CharacterMapRule;
import smalleditor.common.rules.ExtendedWordRule;
import smalleditor.editors.common.CommonWhitespaceDetector;
import smalleditor.editors.common.CommonWordDetector;
import smalleditor.preferences.ColorManager;
import smalleditor.preferences.PreferenceNames;

public class HtmlDoctypeScanner extends RuleBasedScanner {
	private ColorManager colorManager = ColorManager.getDefault();

	private static final class TagStartRule extends ExtendedWordRule {
		private Pattern pattern;

		private TagStartRule(IWordDetector detector, IToken defaultToken,
				boolean ignoreCase) {
			super(detector, defaultToken, ignoreCase);
		}

		@Override
		protected boolean wordOK(String word, ICharacterScanner scanner) {
			if (pattern == null) {
				pattern = Pattern.compile("<(/)?"); //$NON-NLS-1$
			}
			return pattern.matcher(word).matches();
		}
	}

	private static final class TagStartWordDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return c == '<';
		}

		public boolean isWordPart(char c) {
			return c == '/';
		}
	}

	public HtmlDoctypeScanner() {
		List<IRule> rules = new ArrayList<IRule>();

		// Add rule for double quotes
		rules.add(new MultiLineRule(
				"\"", "\"", createToken("string.quoted.double.doctype.identifiers-and-DTDs.html"), '\\')); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// Add a rule for single quotes
		rules.add(new MultiLineRule(
				"'", "'", createToken("string.quoted.single.doctype.identifiers-and-DTDs.html"), '\\')); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new CommonWhitespaceDetector()));

		// Tags
		WordRule wordRule = new WordRule(new CommonWordDetector(),
				createToken(""), true);
		wordRule.addWord("DOCTYPE", createToken("entity.name.tag.doctype.html")); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(wordRule);

		CharacterMapRule rule = new CharacterMapRule();
		rule.add('>', createToken(HtmlTokenType.TAG_END));
		rule.add('=', createToken(HtmlTokenType.EQUAL));
		rules.add(rule);
		// Tag start <(/)?
		rules.add(new TagStartRule(new TagStartWordDetector(),
				createToken(HtmlTokenType.TAG_START), false));

		setRules(rules.toArray(new IRule[rules.size()]));
		setDefaultReturnToken(createToken(""));
	}

	/**
	 * createToken
	 * 
	 * @param type
	 * @return
	 */
	protected IToken createToken(HtmlTokenType type) {
		return this.createToken(type.getScope());
	}

	/**
	 * createToken
	 * 
	 * @param string
	 * @return
	 */
	protected IToken createToken(String string) {
		if(string == "entity.name.tag.doctype.html" || string == HtmlTokenType.TAG_END.getScope() || string == HtmlTokenType.TAG_START.getScope()) {
			return new Token(new TextAttribute(
					colorManager.getColorFromPreferencesKey(PreferenceNames.P_KEYWORD_COLOR), null, SWT.BOLD));
		}
		
		return new Token(string);
	}
}
