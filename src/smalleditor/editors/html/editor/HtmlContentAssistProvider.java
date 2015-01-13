package smalleditor.editors.html.editor;

import smalleditor.editors.common.editor.CommonContentAssistProvider;
import smalleditor.util.xml.resourceReader;

public class HtmlContentAssistProvider extends CommonContentAssistProvider{

	public HtmlContentAssistProvider() {
		super();

		this.elementsList = resourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/html.xml"));
	}

}