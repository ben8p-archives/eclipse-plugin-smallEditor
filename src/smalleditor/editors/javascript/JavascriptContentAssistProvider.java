package smalleditor.editors.javascript;

import smalleditor.editors.common.CommonContentAssistProvider;
import smalleditor.util.xml.ResourceReader;

public class JavascriptContentAssistProvider extends CommonContentAssistProvider{

	public JavascriptContentAssistProvider() {
		super();

		this.elementsList = ResourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/javascript.xml"));
	}

}