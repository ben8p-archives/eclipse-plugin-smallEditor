/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.javascript;

import org.eclipse.jface.text.IDocument;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;

public class JavascriptDocumentTokenBuilder extends DocumentTokenBuilder {

	public JavascriptDocumentTokenBuilder(IDocument document) {
		super(document);
		this.setElements(
			//new String[] {"'", "\"", "\\", "function", "\n", "todo", "fixme", Pattern.quote("."), Pattern.quote(","), Pattern.quote("{"), Pattern.quote("}"), Pattern.quote("("), Pattern.quote(")"), Pattern.quote("["), Pattern.quote("]"), ":", ";", "=", Pattern.quote("/*"), Pattern.quote("*/"), "//"},
			new String[] {"'", "\"", "\\", "function", "\n", "todo", "fixme", ".", ",", "{", "}", "(", ")", "[", "]", ":", ";", "=", "/*", "*/", "//"},
			new DocumentNodeType[] {DocumentNodeType.SingleQuoteString, DocumentNodeType.String, DocumentNodeType.EscapeChar, DocumentNodeType.Function, DocumentNodeType.NewLine, DocumentNodeType.Todo, DocumentNodeType.Fixme, null, DocumentNodeType.Comma, DocumentNodeType.OpenObject, DocumentNodeType.CloseObject, null, null, DocumentNodeType.OpenArray, DocumentNodeType.CloseArray, DocumentNodeType.Colon, null, null, DocumentNodeType.OpenMultilineComment, DocumentNodeType.CloseMultilineComment, DocumentNodeType.OneLineComment}
		);
	}
	
	@Override
	protected DocumentNode createDefaultNode(DocumentNodeType type, int offset,
			int length, String expression) {

		if(type == DocumentNodeType.Function) {
			//go till next block
			return createEOBNode(type, offset, expression, DocumentNodeType.OpenObject, false);
		}
		
		return super.createDefaultNode(type, offset, length, expression);
	}
	
	
}
