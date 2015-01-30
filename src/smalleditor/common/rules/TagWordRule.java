/**
 * Aptana Studio
 * Copyright (c) 2005-2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.common.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Max Stepanov
 */
public class TagWordRule extends AExtendedWordRule {

	/**
	 * @param detector
	 * @param defaultToken
	 * @param ignoreCase
	 */
	public TagWordRule(IWordDetector detector, IToken defaultToken,
			boolean ignoreCase) {
		super(detector, defaultToken, ignoreCase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.rules.WordRule#evaluate(org.eclipse.jface.text
	 * .rules.ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		if (scanner.getColumn() > 0) {
			scanner.unread();
			int c = scanner.read();
			if (c == '<') {
				return super.evaluate(scanner);
			} else if ((c == '!' || c == '/') && scanner.getColumn() > 1) {
				scanner.unread();
				scanner.unread();
				c = scanner.read();
				scanner.read();
				if (c == '<') {
					return super.evaluate(scanner);
				}
			}
		}
		return Token.UNDEFINED;
	}

}
