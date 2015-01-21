package smalleditor.editors.common.editor;

import org.eclipse.jface.text.*;

import java.util.*;
import org.eclipse.jface.text.rules.*;

import org.eclipse.swt.graphics.Color;

import smalleditor.editors.common.editor.CommonWhitespaceDetector;
import smalleditor.preference.ColorManager;
import smalleditor.preference.PreferenceNames;


public class CommonStringScanner extends RuleBasedScanner {
	/**
	 * Creates a new JSFuncScanner object.
	 * 
	 * @param manager
	 */
	public CommonStringScanner() {
		ColorManager colorManager = new ColorManager();
		Color aColor = colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR);
		
		IToken string = new Token(new TextAttribute(aColor));
		Vector rules = new Vector();

		// Add rule for single and double quotes
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new CommonWhitespaceDetector()));

		IRule[] result = new IRule[rules.size()];
		rules.copyInto(result);
		setRules(result);
		
		this.setDefaultReturnToken(new Token(new TextAttribute(
				aColor)));
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public IToken nextToken() {
		return super.nextToken();
	}
}