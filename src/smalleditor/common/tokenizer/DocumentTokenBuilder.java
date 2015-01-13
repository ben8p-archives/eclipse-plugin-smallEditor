/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.common.tokenizer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class DocumentTokenBuilder {
	private IDocument document;
	private List<DocumentNode> nodes;
	private String[] elements = null;
	private DocumentNodeType[] elementsType = null;
	private DocumentTokenizer tokenizer;
	
	public DocumentTokenBuilder(IDocument document) {
		super();
		this.document = document;
	}
	protected void setElements(String[] newElements, DocumentNodeType[] newElementsType) {
		elements = newElements;
		elementsType = newElementsType;
	}
	
	public List<DocumentNode> buildNodes() {
		
		nodes = new LinkedList<DocumentNode>();
		
		if(elements != null) {
		
			int fromIndex = 0;
			DocumentNode node = null;
			
			tokenizer = new DocumentTokenizer(document, elements);
			while(tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				
				DocumentNodeType type = getNodeType(token, tokenizer.getPreviousToken(), tokenizer.getNextToken());
				DocumentNodeType nextType = getNodeType(tokenizer.getNextToken());
				DocumentNodeType previousType = getNodeType(tokenizer.getPreviousToken());
				
				if(type == null) {
					fromIndex += token.length();
					continue;
				}
				
				
				fromIndex = document.get().indexOf(token, fromIndex);
				
//				try {
//					String lineContent = 
//						document.get(
//							document.getLineOffset(
//								document.getLineOfOffset(fromIndex)
//							),
//							document.getLineLength(
//								document.getLineOfOffset(fromIndex)
//							)
//						)
//					;
//					if(lineContent.contains("todo")) {
//						System.out.println(lineContent);
//					}
//				} catch (BadLocationException e) {
//				}
				
				
				int length = token.length();
				
				if(type == DocumentNodeType.OneLineComment && nextType != DocumentNodeType.Todo && nextType != DocumentNodeType.Fixme) {
					node = createEOLNode(type, fromIndex, token);
				} else if(type == DocumentNodeType.OpenMultilineComment && nextType != DocumentNodeType.Todo && nextType != DocumentNodeType.Fixme) {
					node = createEOBNode(type, fromIndex, token, DocumentNodeType.CloseMultilineComment);
				} else if(type == DocumentNodeType.Todo || type == DocumentNodeType.Fixme) {
					node = createEOBNode(type, fromIndex, token, previousType == DocumentNodeType.OneLineComment ? DocumentNodeType.NewLine : DocumentNodeType.CloseMultilineComment);
				} else {
					node = createDefaultNode(type, fromIndex, length, token);
				}
				
				if (node != null) {
					nodes.add(node);
				}
				fromIndex += token.length();
			}
		}
		return nodes;
	}
	
	protected DocumentNode createDefaultNode(DocumentNodeType type, int offset, int length, String expression) {
		return createNode(type, offset, length, expression);
	}
	
	protected DocumentNode createNode(DocumentNodeType type, int offset, int length, String expression) {
		DocumentNode node = new DocumentNode(type);
		node.setPosition(offset, length);
		try {
			node.setLine(document.getLineOfOffset(offset));
		} catch (BadLocationException e) {
			node.setLine(-1);
		}
		
		node.setValue(expression);
		return node;
	}
	
	//end of block
	protected DocumentNode createEOBNode(DocumentNodeType type, int offset, String expression, DocumentNodeType closeType) {
		//go til the end
		int length = 0;
		while(tokenizer.hasMoreTokens()) {
			String eobToken = tokenizer.nextToken();
			DocumentNodeType eobType = getNodeType(eobToken, tokenizer.getPreviousToken(), tokenizer.getNextToken());
			if(eobType == closeType) {
				tokenizer.previousToken();
				
				length = (document.get().indexOf(eobToken, offset) - offset) /*- eobToken.length()*/;
				
				break;
			}
			expression += " " + eobToken;
		}
		return createNode(type, offset, length, expression);
	}
	
	//end of line
	protected DocumentNode createEOLNode(DocumentNodeType type, int offset, String expression) {
		return createEOBNode(type, offset, expression, DocumentNodeType.NewLine);
	}
	
	protected DocumentNodeType getNodeType(String token, String previousToken, String nextToken) {
		DocumentNodeType type = getNodeType(token);
		//check the previous to be sure we are in a todo or fixme
		if((type == DocumentNodeType.Todo || type == DocumentNodeType.Fixme) && getNodeType(previousToken) != DocumentNodeType.OneLineComment && getNodeType(previousToken) != DocumentNodeType.OpenMultilineComment ) {
			return null;
		}
		return type;
	}
	protected DocumentNodeType getNodeType(String token) {
		if(token == null) {
			return null;
		}
		int position = Arrays.asList(elements).indexOf(token.toLowerCase());
		if(position == -1) { return null; }
		return elementsType[position];
	}

	
}
