/**
 *
 */
package smalleditor.editors.common.model.reader;

import static smalleditor.util.CharUtility.isWhiteSpace;
import static smalleditor.util.CharUtility.isNewLine;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * JsonDocReader used to read an IDoc.
 *
 * @author Matt Garner
 *
 */
public class DocumentReader implements Reader {

	private IDocument doc;

	private char previous;

	private char current;

	private int position;

	public DocumentReader(IDocument doc) {
		super();
		this.doc = doc;
	}

	public char getCurrent() {
		return current;
	}

	public char getNextChar() throws Exception {
		char ch = next();
		previous = current;
		current = ch;
		return current;
	}

	private char next() throws Exception {

		try {
			if (position < doc.getLength()) {
				char ch = (char) doc.getChar(position++);

				return ch;
			} else {
				return (char) -1;
			}
		} catch (BadLocationException e) {
			throw new Exception();
		}
	}

	public char getNextClean() throws Exception {

		char ch = ' ';
		while (isWhiteSpace(ch) && !isNewLine(ch)) {
			ch = next();
		}
		previous = current;
		current = ch;
		return current;
	}

	public int getPosition() {
		return position - 1;
	}

	public char getPrevious() {
		return previous;
	}
}
