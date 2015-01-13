package smalleditor.editors.html.editor;

import org.eclipse.jface.text.rules.IWordDetector;

import smalleditor.util.CharUtility;

/**
 * A JavaScript aware word detector. JavaScript tokens are almost identical to
 * Java so this class is borrowed from
 * org.eclipse.jdt.internal.ui.text.JavaWordDetector.
 */
public class HtmlWordDetector implements IWordDetector {

	/**
	 * @see IWordDetector#isWordStart JavaScript tokens are almost identical to
	 *      Java so for now we can just borrow this behavior.
	 */
	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierStart(c) || CharUtility.slash == c || CharUtility.lowerThan == c;
	}

	/**
	 * @see IWordDetector#isWordPart JavaScript tokens are almost identical to
	 *      Java so for now we can just borrow this behavior.
	 */
	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c) || CharUtility.tab == c || CharUtility.space == c || CharUtility.greaterThan == c;
	}
}