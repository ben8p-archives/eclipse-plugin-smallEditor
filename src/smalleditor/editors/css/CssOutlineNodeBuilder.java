/*
 * CssOutlineNodeBuilder.java	Created on 12 Feb 2015
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

import smalleditor.editors.common.outline.CommonOutlineClassNameElement;
import smalleditor.editors.common.parsing.AOutlineNodeBuilder;
import smalleditor.tokenizer.DocumentNode;
import smalleditor.tokenizer.DocumentNodeType;
import smalleditor.tokenizer.DocumentType;
import smalleditor.utils.TextUtility;

public class CssOutlineNodeBuilder extends AOutlineNodeBuilder {
	private int deep = 0;
	private HashMap tree = new HashMap();
	private Boolean canGoDeeper = true;
	
	public CssOutlineNodeBuilder() {
		super();
		setScanner((CssDocumentTokenBuilder) CssDocumentTokenBuilder.getDefault(DocumentType.CSS));
	}
	@Override
	protected List getSyntacticElements(IDocument document, List<DocumentNode> nodes) {
		deep = 0;
		return super.getSyntacticElements(document, nodes);
	}
	
	@Override
	protected Object processToken(IDocument document, DocumentNode node, DocumentNode previousNode, String expression, int offset, int length) {
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
				object = (CommonOutlineClassNameElement) addClassName(document, expression, offset, length);
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
			e.printStackTrace();
		}
		if(deep > 1) {
			object = null;
		}
		return object;
	}
	
	private Object addClassName(IDocument document, String expression, int offset, int length) throws BadLocationException {
		String className = TextUtility.EMPTY_STRING;
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

}
