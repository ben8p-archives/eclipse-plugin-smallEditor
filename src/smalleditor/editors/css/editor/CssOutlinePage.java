/*
 * CssContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.css.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import smalleditor.editors.common.editor.CommonOutlineFunctionElement;
import smalleditor.editors.common.editor.CommonOutlinePage;
import smalleditor.util.CharUtility;

public class CssOutlinePage extends CommonOutlinePage {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public CssOutlinePage(IDocument document) {
		super(document);
	}

	@Override
	protected RuleBasedScanner getScanner() {
		CssOutlineScanner scanner = new CssOutlineScanner();
		return scanner;
	}

	@Override
	protected Object processToken(IToken token, String expression, int offset, int length) {
		try {
			if (token.equals(CssOutlineScanner.TOKEN_CLASSNAME)) {
				return addClassName(expression, offset, length);
			}
		} catch (BadLocationException e) {
		}		
		return null;
	}
	
	private Object addClassName(String expression, int offset, int length) throws BadLocationException {
		System.out.println(expression);

		return null;
//		String functionSignature = getNaked(expression);
//		int braceOffset = functionSignature.indexOf("(");
//		String functionName = functionSignature.substring(0, braceOffset).trim();
//		String arguments = functionSignature.substring(
//				functionSignature.indexOf("("),
//				functionSignature.indexOf(")") + 1);
//
//		if(functionName.equals("")) {
//			int line = document.getLineOfOffset(offset);
//			int lineOffset = document.getLineOffset(line);
//			String lineStr = document.get(lineOffset, offset - lineOffset);
//			String[] lineElements = lineStr.replaceAll("(\\w+)", " $1 ").replaceAll("\\s+", " ").split(" ");
//			int cursor = lineElements.length;
//			Boolean pickupNext = false;
//			while(--cursor >= 0) {
//				System.out.println(lineElements[cursor]);
//				if(!lineElements[cursor].trim().equals("")) {
//					
//					if(lineElements[cursor].equals(Character.toString(CharUtility.colon)) || lineElements[cursor].equals(Character.toString(CharUtility.equal))) {
//						pickupNext = true;
//						continue;
//					}
//					if(pickupNext) {
//						functionName = lineElements[cursor];
//						if(!functionName.matches("\\w+")) {
//							functionName = "";
//						}
//						break;
//					}
//				}
//			}
//			
//			
//			System.out.println(functionName);
//		}
//		
//
//		CommonOutlineFunctionElement aFunction = new CommonOutlineFunctionElement(functionName, arguments, offset, length);
//
//		return aFunction;

		
	}

	
	/**
	 * Method getNaked.
	 * @param funcName
	 */
//	private String getNaked(String funcName) {
//		if (funcName == null) {
//			return null;
//		}
//
//		funcName = funcName.trim().substring(FUNCTION.length()).trim();
//		funcName = CharUtility.replaceInString(funcName.trim(), LINE_SEPARATOR, "");
//
//		StringBuffer strBuf = new StringBuffer("");
//		int len = funcName.length();
//		boolean wasSpace = false;
//		for (int i = 0; i < len; i++) {
//			char ch = funcName.charAt(i);
//			if (ch == ' ') {
//				wasSpace = true;
//			} else // not space
//			{
//				if (wasSpace) {
//					strBuf.append(' ');
//				}
//				strBuf.append(ch);
//				wasSpace = false;
//			}
//		}
//		return strBuf.toString();
//	}
	
}
