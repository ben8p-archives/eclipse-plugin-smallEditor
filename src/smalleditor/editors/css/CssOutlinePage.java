package smalleditor.editors.css;

import smalleditor.editors.common.ACommonOutlinePage;
import smalleditor.editors.common.outline.CommonOutlineElementList;
import smalleditor.editors.common.parsing.AOutlineBuilder;
import smalleditor.editors.css.parsing.CssOutlineBuilder;

public class CssOutlinePage extends ACommonOutlinePage {
	private AOutlineBuilder outlineBuilder = new CssOutlineBuilder();
	
	@Override
	protected CommonOutlineElementList getNodes() {
		return outlineBuilder.buildOutline(getDocument());
	}

}
