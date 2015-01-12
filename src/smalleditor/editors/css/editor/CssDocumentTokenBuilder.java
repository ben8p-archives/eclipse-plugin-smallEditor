/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.css.editor;

import org.eclipse.jface.text.IDocument;

import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;

public class CssDocumentTokenBuilder extends DocumentTokenBuilder {

	public CssDocumentTokenBuilder(IDocument document) {
		super(document);
		this.setElements(
			new String[] {"todo", "fixme", "\n", "{", "}", ",", ":", ";", "/*", "*/"},
			new DocumentNodeType[] {DocumentNodeType.Todo, DocumentNodeType.Fixme, DocumentNodeType.NewLine, DocumentNodeType.OpenObject, DocumentNodeType.CloseObject, DocumentNodeType.Comma, DocumentNodeType.Colon, null, DocumentNodeType.OpenMultilineComment, DocumentNodeType.CloseMultilineComment}
		);
	}
	
	
}
