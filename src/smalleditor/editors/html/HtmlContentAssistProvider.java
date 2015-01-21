package smalleditor.editors.html;

import smalleditor.editors.common.CommonContentAssistProvider;
import smalleditor.util.xml.ResourceReader;

public class HtmlContentAssistProvider extends CommonContentAssistProvider{

	public HtmlContentAssistProvider() {
		super();

		this.elementsList = ResourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/html.xml"));
	}

}