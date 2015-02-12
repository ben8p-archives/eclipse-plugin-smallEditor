package smalleditor.editors.json;

import smalleditor.editors.common.ACommonContentAssistProvider;
import smalleditor.xml.ResourceReader;

public class JsonContentAssistProvider extends ACommonContentAssistProvider{

	public JsonContentAssistProvider() {
		super();

		this.elementsList = ResourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/json.xml"));
	}

}