package smalleditor.editors.javascript.editor;

import smalleditor.editors.common.editor.CommonContentAssistProvider;
import smalleditor.util.xml.resourceReader;

public class JavascriptContentAssistProvider extends CommonContentAssistProvider{

	public JavascriptContentAssistProvider() {
		super();

		this.elementsList = resourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/javascript.xml"));
	}

}