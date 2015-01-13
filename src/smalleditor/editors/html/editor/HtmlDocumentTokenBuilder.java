/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.html.editor;

import org.eclipse.jface.text.IDocument;

import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;

public class HtmlDocumentTokenBuilder extends DocumentTokenBuilder {

	public HtmlDocumentTokenBuilder(IDocument document) {
		super(document);
		this.setElements(
			new String[] {"\n", "todo", "fixme", "<!--", "-->"},
			new DocumentNodeType[] {DocumentNodeType.NewLine, DocumentNodeType.Todo, DocumentNodeType.Fixme, DocumentNodeType.OpenMultilineComment, DocumentNodeType.CloseMultilineComment}
		);
	}
	
	protected DocumentNodeType getNodeType(String token, String previousToken, String nextToken) {
		DocumentNodeType type = super.getNodeType(token, previousToken, nextToken);
		if(type == DocumentNodeType.OpenObject && getNodeType(nextToken) == DocumentNodeType.CloseObject) {
			type = null;
		}else if(type == DocumentNodeType.CloseObject && getNodeType(previousToken) != DocumentNodeType.OpenObject) {
			type = null;
		}
		return type;
	}
	
}
