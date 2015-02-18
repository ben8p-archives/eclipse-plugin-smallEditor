/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.html;

import smalleditor.tokenizer.DocumentNodeType;
import smalleditor.tokenizer.DocumentTokenBuilder;
import smalleditor.utils.IConstants;

public class HtmlDocumentTokenBuilder extends DocumentTokenBuilder {

	public HtmlDocumentTokenBuilder() {
		super();
		this.setElements(
			new String[] {"\n", IConstants.TODO_IDENTIFIER, IConstants.FIXME_IDENTIFIER, "<!--", "-->"},
			new DocumentNodeType[] {DocumentNodeType.NewLine, DocumentNodeType.Todo, DocumentNodeType.Fixme, DocumentNodeType.OpenMultilineComment, DocumentNodeType.CloseMultilineComment}
		);
	}
	
//	protected DocumentNodeType getNodeType(String token, String previousToken, String nextToken) {
//		DocumentNodeType type = super.getNodeType(token, previousToken, nextToken);
//		if(type == DocumentNodeType.OpenObject && getNodeType(nextToken) == DocumentNodeType.CloseObject) {
//			//current is not an open, but the begining of the close
//			type = null;
//		}else if(type == DocumentNodeType.CloseObject && getNodeType(previousToken) != DocumentNodeType.OpenObject) {
//			type = null;
//		}
//		return type;
//	}
	
}
