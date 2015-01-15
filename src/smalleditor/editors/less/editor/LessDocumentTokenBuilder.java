/*
 * StringTokenizer.java	Created on 12 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.less.editor;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.css.editor.CssDocumentTokenBuilder;

public class LessDocumentTokenBuilder extends CssDocumentTokenBuilder {

	public LessDocumentTokenBuilder(IDocument document) {
		super(document);
	}
	
}
