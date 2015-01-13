/*******************************************************************************
 * Copyright (c) 2005 Prashant Deva.
 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License - v 1.0
 * which is available at http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/

package smalleditor.common.rule;

import org.eclipse.jface.text.rules.*;

public class XmlTagRule extends WordPatternRule {

	public XmlTagRule(IWordDetector detector, String end, IToken token) {
		super(detector, "<", end, token);
	}
	protected boolean sequenceDetected(
		ICharacterScanner scanner,
		char[] sequence,
		boolean eofAllowed) {
		int c = scanner.read();
		if (sequence[0] == '<') {
			if (c == '?') {
				// processing instruction - abort
				scanner.unread();
				return false;
			}
			if (c == '!') {
				scanner.unread();
				// comment - abort
				return false;
			}
		} else if (sequence[0] == '>') {
			scanner.unread();
		}
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}
	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		Boolean ret = super.endSequenceDetected(scanner);
		scanner.unread();
		int c = scanner.read();
		//c = scanner.read();
		
		System.out.println("endSequenceDetected:" + Character.toChars(c)[0]);
		if(c == ' ' || c == '>' || c == '\t') {
			//scanner.unread();
			return true;
		}
		return ret;
		
	}
}
