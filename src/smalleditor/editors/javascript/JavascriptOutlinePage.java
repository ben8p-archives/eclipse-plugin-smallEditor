/*
 * JavascriptContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.javascript;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;
import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.CommonOutlineFunctionElement;
import smalleditor.editors.common.CommonOutlinePage;
import smalleditor.utils.CharUtility;
import smalleditor.utils.TextUtility;

public class JavascriptOutlinePage extends CommonOutlinePage {
	public static final String FUNCTION = "function";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	//protected HashMap functions = new HashMap();
	
	public JavascriptOutlinePage(IDocument document) {
		super(document);
	}

	@Override
	protected DocumentTokenBuilder getScanner() {
		JavascriptDocumentTokenBuilder scanner = (JavascriptDocumentTokenBuilder) JavascriptDocumentTokenBuilder.getDefault(DocumentType.JS);
		return scanner;
	}

	@Override
	protected Object processToken(DocumentNode node, DocumentNode previousNode, String expression, int offset, int length) {
		try {
			if (node.getType() == DocumentNodeType.Function) {
				return addFunction(expression, offset, length);
			}
		} catch (BadLocationException e) {
		}		
		return null;
	}
	
	private Object addFunction(String expression, int offset, int length) throws BadLocationException {
		String functionSignature;
		try {
			functionSignature = getNaked(expression);
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
		int braceOffset = functionSignature.indexOf("(");
		String functionName = functionSignature.substring(0, braceOffset).trim();
		String arguments = functionSignature.substring(
				functionSignature.indexOf("("),
				functionSignature.indexOf(")") + 1);

		if(functionName.equals("")) {
			int line = document.getLineOfOffset(offset);
			int lineOffset = document.getLineOffset(line);
			String lineStr = document.get(lineOffset, offset - lineOffset);
			String[] lineElements = lineStr.replaceAll("(\\w+)", " $1 ").replaceAll("\\s+", " ").split(" ");
			int cursor = lineElements.length;
			Boolean pickupNext = false;
			while(--cursor >= 0) {
//				System.out.println(lineElements[cursor]);
				if(!lineElements[cursor].trim().equals("")) {
					
					if(lineElements[cursor].equals(Character.toString(CharUtility.colon)) || lineElements[cursor].equals(Character.toString(CharUtility.equal))) {
						pickupNext = true;
						continue;
					}
					if(pickupNext) {
						functionName = lineElements[cursor];
						if(!functionName.matches("\\w+")) {
							functionName = "";
						}
						break;
					}
				}
			}
			
			
//			System.out.println(functionName);
		}
		
		//if (!functions.containsKey(functionName)) {
		CommonOutlineFunctionElement aFunction = new CommonOutlineFunctionElement(functionName, arguments, offset, length);

		
		//functions.put(functionName, aFunction);

		//detectFunctionContext(aFunction);
		
		return aFunction;
		//}
		//return null;
		
	}

//	private void parseFunctionContext(CommonOutlineFunctionElement aFunction) {
//		IToken token;
//
//		token = nextNonWhitespaceToken();
//		while (!token.isEOF()) {
//			int offset = scanner.getTokenOffset();
//			int length = scanner.getTokenLength();
//			String expression = getExpression(offset, length);
//
//			if (expression.equals("}")) {
//				return;
//			} else if (expression.equals("{")) {
//				parseFunctionContext(aFunction);
//			}
//
//			token = nextNonWhitespaceToken();
//		}
//	}
//	
//	private void detectFunctionContext(CommonOutlineFunctionElement aFunction) {
//		IToken token = nextNonWhitespaceToken();
//		while (!token.isEOF()) {
//			int offset = scanner.getTokenOffset();
//			int length = scanner.getTokenLength();
//			String expression = getExpression(offset, length);
//
//			if (expression.equals("{")) {
//				parseFunctionContext(aFunction);
//				return;
//			}
//
//			token = nextNonWhitespaceToken();
//		}
//	}
	
	/**
	 * Method getNaked.
	 * @param funcName
	 */
	private String getNaked(String funcName) throws StringIndexOutOfBoundsException {
		if (funcName == null) {
			return null;
		}

		funcName = funcName.trim().substring(FUNCTION.length()).trim();
		funcName = TextUtility.replace(funcName.trim(), LINE_SEPARATOR, "");

		StringBuffer strBuf = new StringBuffer("");
		int len = funcName.length();
		boolean wasSpace = false;
		for (int i = 0; i < len; i++) {
			char ch = funcName.charAt(i);
			if (ch == ' ') {
				wasSpace = true;
			} else // not space
			{
				if (wasSpace) {
					strBuf.append(' ');
				}
				strBuf.append(ch);
				wasSpace = false;
			}
		}
		return strBuf.toString();
	}
	
}
