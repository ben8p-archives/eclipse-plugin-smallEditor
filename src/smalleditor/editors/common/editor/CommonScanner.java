package smalleditor.editors.common.editor;

import java.util.*;
import org.eclipse.jface.text.rules.*;
//import org.eclipse.jface.text.*;

import org.eclipse.swt.graphics.Color;


/**
 * 
 * 
 * @author $Author: agfitzp $, $Date: 2003/05/28 15:17:12 $
 * 
 * @version $Revision: 1.1 $
 */
public class CommonScanner extends RuleBasedScanner {
	/**
	 * Creates a new JSScanner object.
	 * 
	 * @param manager
	 */
	public CommonScanner(Color aColor) {
		List rules = new ArrayList();
		// IToken procInstr = new Token(new TextAttribute(aColor));

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new CommonWhitespaceDetector()));

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

}