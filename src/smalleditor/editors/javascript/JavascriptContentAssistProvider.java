package smalleditor.editors.javascript;

import smalleditor.editors.common.ACommonContentAssistProvider;
import smalleditor.xml.ResourceReader;

public class JavascriptContentAssistProvider extends ACommonContentAssistProvider{

	public JavascriptContentAssistProvider() {
		super();

		this.elementsList = ResourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/javascript.xml"));
	}

}