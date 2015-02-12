/*
 * CssOutlineNodeBuilder.java	Created on 12 Feb 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
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

import smalleditor.editors.common.outline.ACommonOutlineElement;
import smalleditor.editors.common.outline.CommonOutlineBaseElement;
import smalleditor.editors.common.outline.CommonOutlineBlockElement;
import smalleditor.editors.common.parsing.AOutlineNodeBuilder;
import smalleditor.tokenizer.DocumentNode;
import smalleditor.tokenizer.DocumentNodeType;
import smalleditor.tokenizer.DocumentType;
import smalleditor.utils.TextUtility;

public class JsonOutlineNodeBuilder extends AOutlineNodeBuilder {
	private int deep = 0;
	private HashMap tree = new HashMap();
	
	public JsonOutlineNodeBuilder() {
		super();
		setScanner((JsonDocumentTokenBuilder) JsonDocumentTokenBuilder.getDefault(DocumentType.JSON));
	}
	@Override
	protected List getSyntacticElements(IDocument document, List<DocumentNode> nodes) {
		deep = 0;
		return super.getSyntacticElements(document, nodes);
	}
	
	@Override
	protected Object processToken(IDocument document, DocumentNode node, DocumentNode previousNode, String expression, int offset, int length) {
		ACommonOutlineElement object = null;
		List<ACommonOutlineElement> elements = null;
		try {
			if (node.getType() == DocumentNodeType.OpenObject) {
				if(previousNode != null && previousNode.getType() == DocumentNodeType.Colon) {
					//open object wins over colon
					elements = (List) tree.get(deep);
					if(elements != null) {
						for(ACommonOutlineElement element: elements) {
							element.removeLastChildElement();
						}
					}
				}
				
				
				deep++;
			
				object = (ACommonOutlineElement) addBlock(document, expression, offset, length);
				if(object != null) {
					elements = (List) tree.get(deep);
					if(elements == null) {
						elements = new ArrayList<ACommonOutlineElement>();
						tree.put(deep, elements);
					}
					elements.add(object);
				}
			}
			
			if (node.getType() == DocumentNodeType.Colon) {
				object = (ACommonOutlineElement) addElement(document, expression, offset, length);
				if(object != null) {
					elements = (List) tree.get(deep);
					if(elements != null) {
						for(ACommonOutlineElement element: elements) {
							element.addChildElement(object);
							object.setParent(element);
						}
					}
					object = null;
				}
			}

			if (node.getType() == DocumentNodeType.CloseObject) {
				List<ACommonOutlineElement> parents = (List) tree.get(deep - 1);
				elements = (List) tree.get(deep);
				
				if(parents != null && elements != null) {
					for(ACommonOutlineElement element: elements) {
						for(ACommonOutlineElement parent: parents) {
							if(parent.category() == ACommonOutlineElement.BLOCK) {
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
			e.printStackTrace();
		}
		if(deep > 1) {
			object = null;
		}
		return object;
	}
	
	private String getElementName(IDocument document, String expression, int offset, int length) throws BadLocationException {
		int line = document.getLineOfOffset(offset);
		int lineOffset = document.getLineOffset(line);
		String lineStr = document.get(lineOffset, offset - lineOffset);

		return lineStr.trim().equals(TextUtility.EMPTY_STRING) ? expression : lineStr.replaceAll(":", TextUtility.EMPTY_STRING).replaceAll("\"", TextUtility.EMPTY_STRING).trim();
	}
	private CommonOutlineBaseElement addElement(IDocument document, String expression, int offset, int length) throws BadLocationException {

		return new CommonOutlineBaseElement(getElementName(document, expression, offset, length), offset, length);
	}
	
	private CommonOutlineBlockElement addBlock(IDocument document, String expression, int offset, int length) throws BadLocationException {
		
		return new CommonOutlineBlockElement(getElementName(document, expression, offset, length), offset, length);
		

		
	}

}
