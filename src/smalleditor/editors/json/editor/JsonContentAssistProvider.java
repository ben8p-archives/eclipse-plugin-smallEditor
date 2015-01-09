package smalleditor.editors.json.editor;

import smalleditor.editors.common.editor.CommonContentAssistProvider;
import smalleditor.util.xml.resourceReader;

public class JsonContentAssistProvider extends CommonContentAssistProvider{

	public JsonContentAssistProvider() {
		super();

		this.elementsList = resourceReader.read(this.getClass().getClassLoader()
				.getResourceAsStream("res/data/json.xml"));
	}

}