/*
 * CssContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.less;

import smalleditor.editors.common.outline.CommonOutlineElementList;
import smalleditor.editors.common.parsing.AOutlineBuilder;
import smalleditor.editors.css.CssOutlinePage;
import smalleditor.editors.less.parsing.LessOutlineBuilder;

public class LessOutlinePage extends CssOutlinePage {
	private AOutlineBuilder outlineBuilder = new LessOutlineBuilder();
	
	@Override
	protected CommonOutlineElementList getNodes() {
		return outlineBuilder.buildOutline(getDocument());
	}
}
