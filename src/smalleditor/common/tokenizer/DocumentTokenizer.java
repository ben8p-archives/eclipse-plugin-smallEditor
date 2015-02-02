package smalleditor.common.tokenizer;

import java.util.regex.Pattern;

import org.eclipse.jface.text.IDocument;

public class DocumentTokenizer {
	private String[] tokens;
	private int cursorPosition = -1;
//	private final String NEWLINE = "NEWLINEDELIMITER_asdf54as5f647af7weafsadfasfa7a8wf7r8aer";
	
	public DocumentTokenizer(IDocument document, String[] wordsToSeparate) {
		String content = document.get();
		
		//content = content.replaceAll("[\t ]+", TextUtility.EMPTY_STRING);
		for(String wordToSeparate: wordsToSeparate) {
			content = content.replaceAll(Pattern.quote(wordToSeparate), " " + (wordToSeparate == "\\" ? "\\\\" : wordToSeparate) + " "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		//content = content.replaceAll("\n", NEWLINE + "\n");
		
		tokens = content.split("[\t ]+"); //$NON-NLS-1$
		
	}

	
	/*Tests if there are more tokens available from this tokenizer's string.*/
	public Boolean hasMoreTokens() {
		return (cursorPosition < tokens.length - 1 );
	}
	
	/*Calculates the number of times that this tokenizer's nextToken method can be called before it generates an exception.*/
	public int countTokens() {
		return tokens.length;
	}
	
	/* go to the next */
	public String nextToken() {
		String token = getNextToken();
		cursorPosition++;
		return token;
	}
	/* go to the previous */
	public String previousToken() {
		String token = getPreviousToken();
		cursorPosition--;
		return token;
	}
	/* get the next, no cursor move */
	public String getNextToken() {
		int newCursor = cursorPosition + 1;
		if(tokens.length <= newCursor) { return null; }
		//tokens[newCursor] = tokens[newCursor].replaceAll(NEWLINE, "\n");
		return tokens[newCursor];
	}
	/* get the previous, no cursor move */
	public String getPreviousToken() {
		int newCursor = cursorPosition - 1;
		if(newCursor < 0) { return null; }
		//tokens[newCursor] = tokens[newCursor].replaceAll(NEWLINE, "\n");
		return tokens[newCursor];
	}
	
}
