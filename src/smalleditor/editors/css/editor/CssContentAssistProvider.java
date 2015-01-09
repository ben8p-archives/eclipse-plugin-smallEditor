package smalleditor.editors.css.editor;

import smalleditor.editors.common.editor.CommonContentAssistProvider;
import smalleditor.util.xml.resourceReader;

public class CssContentAssistProvider extends CommonContentAssistProvider {

	public CssContentAssistProvider() {
		super();

		this.elementsList = resourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/css.xml"));
	}
}