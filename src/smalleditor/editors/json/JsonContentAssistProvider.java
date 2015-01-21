package smalleditor.editors.json;

import smalleditor.editors.common.CommonContentAssistProvider;
import smalleditor.util.xml.ResourceReader;

public class JsonContentAssistProvider extends CommonContentAssistProvider{

	public JsonContentAssistProvider() {
		super();

		this.elementsList = ResourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/json.xml"));
	}

}