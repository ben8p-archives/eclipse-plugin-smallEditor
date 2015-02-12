package smalleditor.editors.html;

import smalleditor.editors.common.ACommonContentAssistProvider;
import smalleditor.xml.ResourceReader;

public class HtmlContentAssistProvider extends ACommonContentAssistProvider{

	public HtmlContentAssistProvider() {
		super();

		this.elementsList = ResourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/html.xml"));
	}

}