/*
 * CssContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.json;

import smalleditor.editors.common.ACommonOutlinePage;
import smalleditor.editors.common.outline.CommonOutlineElementList;
import smalleditor.editors.common.parsing.AOutlineNodeBuilder;

public class JsonOutlinePage extends ACommonOutlinePage {
	private AOutlineNodeBuilder outlineNodeBuilder = new JsonOutlineNodeBuilder();
	
	protected CommonOutlineElementList getNodes() {
		return outlineNodeBuilder.buildOutline(getDocument());
	}


}
