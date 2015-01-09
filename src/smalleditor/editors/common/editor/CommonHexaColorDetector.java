package smalleditor.editors.common.editor;

import org.eclipse.jface.text.rules.IWordDetector;

import smalleditor.util.CharUtility;

/**
 * A JavaScript aware word detector. JavaScript tokens are almost identical to
 * Java so this class is borrowed from
 * org.eclipse.jdt.internal.ui.text.JavaWordDetector.
 */
public class CommonHexaColorDetector implements IWordDetector {

	/**
	 * @see IWordDetector#isWordStart JavaScript tokens are almost identical to
	 *      Java so for now we can just borrow this behavior.
	 */
	public boolean isWordStart(char c) {
		return CharUtility.hash == c;
	}

	/**
	 * @see IWordDetector#isWordPart JavaScript tokens are almost identical to
	 *      Java so for now we can just borrow this behavior.
	 */
	public boolean isWordPart(char c) {
		String hexa = new String(CharUtility.hexaDecimal);
		return hexa.contains(Character.toString(c));
	}
}