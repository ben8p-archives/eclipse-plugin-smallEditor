/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.common.tokenizer;

import java.util.regex.Pattern;

import org.eclipse.jface.text.IDocument;

public class DocumentTokenizer {
	private String[] tokens;
	private int cursorPosition = -1;
	private final String NEWLINE = "NEWLINEDELIMITER_asdf54as5f647af7weafsadfasfa7a8wf7r8aer";
	
	public DocumentTokenizer(IDocument document, String[] wordsToSeparate) {
		String content = document.get();
		
		for(String wordToSeparate: wordsToSeparate) {
			content = content.replaceAll(Pattern.quote(wordToSeparate), " " + wordToSeparate + " ");
		}
		
		content = content.replaceAll("\n", NEWLINE + "\n");
		
		tokens = content.split("\\s+");
		
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
		return tokens[newCursor].replaceAll(NEWLINE, "\n");
	}
	/* get the previous, no cursor move */
	public String getPreviousToken() {
		int newCursor = cursorPosition - 1;
		if(newCursor < 0) { return null; }
		return tokens[newCursor].replaceAll(NEWLINE, "\n");
	}
	
}
