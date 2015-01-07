package smalleditor.editors.common.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;
import smalleditor.util.CharUtility;

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