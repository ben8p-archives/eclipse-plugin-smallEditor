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

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;
import smalleditor.editors.common.editor.CommonOutlineClassNameElement;
import smalleditor.editors.common.editor.CommonOutlinePage;
import smalleditor.util.CharUtility;

public class CssOutlinePage extends CommonOutlinePage {
//	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public CssOutlinePage(IDocument document) {
		super(document);
		this.setSort(false);
	}

	@Override
	protected DocumentTokenBuilder getScanner() {
		CssDocumentTokenBuilder scanner = new CssDocumentTokenBuilder(document);
		return scanner;
	}

	@Override
	protected Object processToken(DocumentNode node, String expression, int offset, int length) {
		try {
			if (node.getType() == DocumentNodeType.OpenObject || node.getType() == DocumentNodeType.Comma) {
				return addClassName(expression, offset, length);
			}
		} catch (BadLocationException e) {
		}
		return null;
	}
	
	private Object addClassName(String expression, int offset, int length) throws BadLocationException {
		String className = "";
		int line = document.getLineOfOffset(offset);
		int lineOffset = document.getLineOffset(line);
		String lineStr = document.get(lineOffset, offset - lineOffset);
		if(lineStr.contains(Character.toString(CharUtility.colon))) {
			return null;
		}
		String[] lineElementsStrings = lineStr.split(",");
		className = lineElementsStrings[lineElementsStrings.length - 1].trim();
		//System.out.println(className);
		
		CommonOutlineClassNameElement aClassName = new CommonOutlineClassNameElement(className, offset, length);
		
		return aClassName;

		
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
