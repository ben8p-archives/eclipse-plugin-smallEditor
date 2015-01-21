package smalleditor.editors.common;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

import smalleditor.utils.CharUtility;

/**
 * 
 * 
 * @author $Author: agfitzp $, $Date: 2003/05/28 15:17:12 $
 * 
 * @version $Revision: 1.1 $
 */
public class CommonWhitespaceDetector implements IWhitespaceDetector {
	/**
	 * 
	 * 
	 * @param c
	 * 
	 * @return
	 */
	public boolean isWhitespace(char c) {
		return CharUtility.isWhiteSpace(c);
	}
}