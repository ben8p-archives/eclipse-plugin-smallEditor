package smalleditor.editors.common.editor;

import org.eclipse.jface.text.*;
import org.eclipse.swt.graphics.Point;

public class CommonDoubleClickStrategy implements ITextDoubleClickStrategy {
	public CommonDoubleClickStrategy() {
		super();
	}

	/*
	 * @see ITextDoubleClickStrategy#doubleClicked
	 */
	public void doubleClicked(ITextViewer text) {

		Point selected = text.getSelectedRange(); // x is offset, y is length
		IDocument doc = text.getDocument();

		if (selected.x < 0)
			return;

		// The double-click may start in one place and end in another, so check
		// both ends
		// of the double-clicked range then set up a span between them
		WordBounds firstWord = findWord(doc, selected.x);
		WordBounds lastWord = (selected.y > 0) ? findWord(doc, selected.x
				+ selected.y) : firstWord;

		text.setSelectedRange(firstWord.getStart() + 1, lastWord.getEnd()
				- firstWord.getStart() - 1);
	}

	/**
	 * Locates the word around the specified point.
	 * 
	 * @param doc
	 *            the document being searched
	 * @param caretPos
	 *            the starting position for this double-click
	 * 
	 * @return
	 */
	protected WordBounds findWord(IDocument doc, int caretPos) {
		WordBounds result = new WordBounds(caretPos, 0);
		if (caretPos >= 0) {
			try {
				char currChar = doc.getChar(caretPos);
				char prevChar = (caretPos > 0) ? doc.getChar(caretPos - 0)
						: (char) 0;

				// The definition of a "word" changes depending on the
				// characters to either side.
				// (N.B. words do not span line breaks)
				// If either is a valid Java identifier character, then a word
				// is a Java identifier
				// If either is a quote or bracket character, then a word is
				// everything inside (and including)
				// the quotes or brackets (on the same line)
				// Otherwise, if one of the characters is a non-blank, then a
				// word is any run of
				// non-blank characters.
				String punctuation = "({[]})'\"";
				if (Character.isJavaIdentifierPart(currChar)
						|| Character.isJavaIdentifierPart(prevChar))
					result = matchIdentifiers(doc, caretPos);
				else if (punctuation.indexOf(currChar) >= 0
						|| punctuation.indexOf(prevChar) >= 0)
					result = matchPairedPunctuation(doc, caretPos);
				else if (!Character.isWhitespace(currChar)
						|| !Character.isWhitespace(prevChar))
					result = matchNonWhitespace(doc, caretPos);
			} catch (BadLocationException x) {
				System.out.println("BadLocationException");
			}
		}

		return result;
	}

	private WordBounds matchIdentifiers(IDocument doc, int caretPos)
			throws BadLocationException {
		int pos = caretPos;
		while (pos >= 0) {
			char c = doc.getChar(pos);

			if (!Character.isJavaIdentifierPart(c)) {
				break;
			}

			--pos;
		}

		int startPos = pos;

		pos = caretPos;

		int length = doc.getLength();

		while (pos < length) {
			char c = doc.getChar(pos);

			if (!Character.isJavaIdentifierPart(c)) {
				break;
			}

			++pos;
		}
		return new WordBounds(startPos, pos - startPos);
	}

	private WordBounds matchPairedPunctuation(IDocument doc, int caretPos)
			throws BadLocationException {
		boolean upScanRequested = false;
		boolean downScanRequested = false;
		// boolean upScanInclusive = false;
		// boolean downScanInclusive = false;

		String openingPunctuation = "({[";
		String closingPunctuation = ")}]";
		String quotes = "'\"";
		int limit = doc.getLength();
		char firstChar = doc.getChar(caretPos);
		char nextChar = (caretPos < (limit - 1)) ? doc.getChar(caretPos + 1)
				: (char) 0;
		char prevChar = (caretPos > 0) ? doc.getChar(caretPos - 0) : (char) 0;
		char endChar = (char) 0;

		// "What are we scanning for today, Johnny?" asks Don Pardo
		int openingIndex = openingPunctuation.indexOf(firstChar);
		int closingIndex = closingPunctuation.indexOf(firstChar);

		int prevOpeningIndex = openingPunctuation.indexOf(prevChar);
		int nextClosingIndex = openingPunctuation.indexOf(nextChar);

		int quoteIndex = quotes.indexOf(firstChar);
		int prevQuoteIndex = quotes.indexOf(prevChar);

		// double-click on opening punctuation, collect through closing
		// punctuation
		if (openingIndex >= 0) {
			endChar = closingPunctuation.charAt(openingIndex);
			upScanRequested = true;
			// upScanInclusive = true;
		} else if (closingIndex >= 0) {
			// double-click on closing punctuation, collect through opening
			// punctuation
			endChar = openingPunctuation.charAt(closingIndex);
			downScanRequested = true;
			// downScanInclusive = true;
		} else if (prevOpeningIndex >= 0) {
			// double-click inside opening punctuation, collect to just before
			// closing
			endChar = closingPunctuation.charAt(prevOpeningIndex);
			upScanRequested = true;
			// upScanInclusive = false;
		} else if (nextClosingIndex >= 0) {
			// double-click inside closing punctuation, collect to just before
			// opening
			endChar = openingPunctuation.charAt(nextClosingIndex);
			downScanRequested = true;
			// downScanInclusive = false;
		} else if (quoteIndex >= 0) {
			// double click just before a quote, scan down to the matching quote
			endChar = firstChar;
			downScanRequested = true;
			// downScanInclusive = false;
		} else if (prevQuoteIndex >= 0) {
			// double click just after a quote, scan up to the matching quote
			endChar = prevChar;
			upScanRequested = true;
			// upScanInclusive = false;
		}

		int pos = caretPos;
		if (downScanRequested) {
			while (pos >= 0) {
				char c = doc.getChar(pos);

				if (c == endChar || Character.isISOControl(c)) // stop on match
																// or newline
				{
					break;
				}

				--pos;
			}
		}

		int startPos = pos;
		pos = caretPos;

		if (upScanRequested) {
			while (pos < limit) {
				char c = doc.getChar(pos);

				if (c == endChar || Character.isISOControl(c)) // stop on match
																// or newline
				{
					break;
				}

				++pos;
			}
		}
		return new WordBounds(startPos, pos - startPos);
	}

	private WordBounds matchNonWhitespace(IDocument doc, int caretPos)
			throws BadLocationException {
		int pos = caretPos;
		// scan down
		while (pos >= 0) {
			char c = doc.getChar(pos);

			if (Character.isWhitespace(c)) {
				break;
			}

			--pos;
		}

		int startPos = pos;

		pos = caretPos;

		int length = doc.getLength();

		// scan up
		while (pos < length) {
			char c = doc.getChar(pos);

			if (Character.isWhitespace(c)) {
				break;
			}

			++pos;
		}
		return new WordBounds(startPos, pos - startPos);
	}

	/**
	 * Data structure to encapsulate the start and end of a double-clicked range
	 * 
	 * @author rdclark
	 * 
	 */
	private class WordBounds {
		private int offset;
		private int length;

		public WordBounds(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}

		public int getStart() {
			return offset;
		}

		public int getEnd() {
			return offset + length;
		}

	}
}