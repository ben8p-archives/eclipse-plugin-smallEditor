/*
 * CssContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;
import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.CommonOutlineClassNameElement;
import smalleditor.editors.common.CommonOutlinePage;

public class CssOutlinePage extends CommonOutlinePage {
//	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private int deep = 0;
	private HashMap tree = new HashMap();
	private Boolean canGoDeeper = true;
	
	public CssOutlinePage(IDocument document) {
		super(document);
	}

	@Override
	protected DocumentTokenBuilder getScanner() {
		CssDocumentTokenBuilder scanner = (CssDocumentTokenBuilder) CssDocumentTokenBuilder.getDefault(DocumentType.CSS);
		return scanner;
	}

	@Override
	protected List getSyntacticElements(List<DocumentNode> nodes) {
		deep = 0;
		return super.getSyntacticElements(nodes);
	}
	
	@Override
	protected Object processToken(DocumentNode node, DocumentNode previousNode, String expression, int offset, int length) {
		CommonOutlineClassNameElement object = null;
		List<CommonOutlineClassNameElement> elements = null;
		try {
			if (node.getType() == DocumentNodeType.OpenObject || node.getType() == DocumentNodeType.Comma) {
				if(node.getType() == DocumentNodeType.Comma && canGoDeeper == true) {
					deep++;
					canGoDeeper = false;
				}
				if(node.getType() == DocumentNodeType.OpenObject) {
					if(canGoDeeper == true) {
						deep++;
					}
					canGoDeeper = true;
				}
				object = (CommonOutlineClassNameElement) addClassName(expression, offset, length);
				if(object != null) {
					elements = (List) tree.get(deep);
					if(elements == null) {
						elements = new ArrayList<CommonOutlineClassNameElement>();
						tree.put(deep, elements);
					}
					elements.add(object);
				}
			}
			if (node.getType() == DocumentNodeType.CloseObject) {
				List<CommonOutlineClassNameElement> parents = (List) tree.get(deep - 1);
				elements = (List) tree.get(deep);
				
				if(parents != null && elements != null) {
					for(CommonOutlineClassNameElement element: elements) {
						for(CommonOutlineClassNameElement parent: parents) {
							parent.addChildElement(element);
							element.setParent(parent);
						}
					}
				}
				tree.remove(deep);
				deep--;
			}
		} catch (BadLocationException e) {
		}
		if(deep > 1) {
			object = null;
		}
		return object;
	}
	
	private Object addClassName(String expression, int offset, int length) throws BadLocationException {
		String className = "";
		int line = document.getLineOfOffset(offset);
		int lineOffset = document.getLineOffset(line);
		String lineStr = document.get(lineOffset, offset - lineOffset);
//		if(lineStr.contains(Character.toString(CharUtility.colon))) {
//			return null;
//		}
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
