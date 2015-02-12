package smalleditor.editors.css;

import smalleditor.editors.common.ACommonOutlinePage;
import smalleditor.editors.common.outline.CommonOutlineElementList;
import smalleditor.editors.common.parsing.AOutlineNodeBuilder;

public class CssOutlinePage extends ACommonOutlinePage {
	private AOutlineNodeBuilder outlineNodeBuilder = new CssOutlineNodeBuilder();
	
	protected CommonOutlineElementList getNodes() {
		return outlineNodeBuilder.buildOutline(getDocument());
	}

}
