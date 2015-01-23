/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.json;

import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;

public class JsonDocumentTokenBuilder extends DocumentTokenBuilder {

	public JsonDocumentTokenBuilder() {
		this.setElements(
			//new String[] {"'", "\"", "\\", "function", "\n", "todo", "fixme", Pattern.quote("."), Pattern.quote(","), Pattern.quote("{"), Pattern.quote("}"), Pattern.quote("("), Pattern.quote(")"), Pattern.quote("["), Pattern.quote("]"), ":", ";", "=", Pattern.quote("/*"), Pattern.quote("*/"), "//"},
			new String[] {"\"", "\\", "\n", ",", "{", "}", "[", "]", ":"},
			new DocumentNodeType[] {DocumentNodeType.String, DocumentNodeType.EscapeChar, DocumentNodeType.NewLine, DocumentNodeType.Comma, DocumentNodeType.OpenObject, DocumentNodeType.CloseObject, DocumentNodeType.OpenArray, DocumentNodeType.CloseArray, DocumentNodeType.Colon}
		);
	}	
	
}
