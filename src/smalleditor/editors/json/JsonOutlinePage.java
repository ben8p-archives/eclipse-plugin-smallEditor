/*
 * CssContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright � 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;
import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.CommonOutlineBaseElement;
import smalleditor.editors.common.CommonOutlineBlockElement;
import smalleditor.editors.common.CommonOutlineElement;
import smalleditor.editors.common.CommonOutlinePage;

public class JsonOutlinePage extends CommonOutlinePage {
//	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private int deep = 0;
	private HashMap tree = new HashMap();
	
	public JsonOutlinePage(IDocument document) {
		super(document);
	}

	@Override
	protected DocumentTokenBuilder getScanner() {
		JsonDocumentTokenBuilder scanner = (JsonDocumentTokenBuilder) JsonDocumentTokenBuilder.getDefault(DocumentType.JSON);
		return scanner;
	}

	@Override
	protected List getSyntacticElements(List<DocumentNode> nodes) {
		deep = 0;
		return super.getSyntacticElements(nodes);
	}
	
	@Override
	protected Object processToken(DocumentNode node, DocumentNode previousNode, String expression, int offset, int length) {
		CommonOutlineElement object = null;
		List<CommonOutlineElement> elements = null;
		try {
			if (node.getType() == DocumentNodeType.OpenObject) {
				if(previousNode != null && previousNode.getType() == DocumentNodeType.Colon) {
					//open object wins over colon
					elements = (List) tree.get(deep);
					if(elements != null) {
						for(CommonOutlineElement element: elements) {
							element.removeLastChildElement();
						}
					}
				}
				
				
				deep++;
			
				object = (CommonOutlineElement) addBlock(expression, offset, length);
				if(object != null) {
					elements = (List) tree.get(deep);
					if(elements == null) {
						elements = new ArrayList<CommonOutlineElement>();
						tree.put(deep, elements);
					}
					elements.add(object);
				}
			}
			
			if (node.getType() == DocumentNodeType.Colon) {
				object = (CommonOutlineElement) addElement(expression, offset, length);
				if(object != null) {
					elements = (List) tree.get(deep);
					if(elements != null) {
						for(CommonOutlineElement element: elements) {
							element.addChildElement(object);
							object.setParent(element);
						}
					}
					object = null;
				}
			}

			if (node.getType() == DocumentNodeType.CloseObject) {
				List<CommonOutlineElement> parents = (List) tree.get(deep - 1);
				elements = (List) tree.get(deep);
				
				if(parents != null && elements != null) {
					for(CommonOutlineElement element: elements) {
						for(CommonOutlineElement parent: parents) {
							if(parent.category() == CommonOutlineElement.BLOCK) {
								parent.addChildElement(element);
								element.setParent(parent);
							}
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
	
	private String getElementName(String expression, int offset, int length) throws BadLocationException {
		int line = document.getLineOfOffset(offset);
		int lineOffset = document.getLineOffset(line);
		String lineStr = document.get(lineOffset, offset - lineOffset);

		return lineStr.trim().equals("") ? expression : lineStr.replaceAll(":", "").replaceAll("\"", "").trim();
	}
	
	private CommonOutlineBaseElement addElement(String expression, int offset, int length) throws BadLocationException {

		return new CommonOutlineBaseElement(getElementName(expression, offset, length), offset, length);
	}
	
	private CommonOutlineBlockElement addBlock(String expression, int offset, int length) throws BadLocationException {
		
		return new CommonOutlineBlockElement(getElementName(expression, offset, length), offset, length);
		

		
	}
	
}