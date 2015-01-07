package smalleditor.editors.json.editor;

import static smalleditor.util.CharUtility.jsonBrackets;
import smalleditor.editors.common.editor.CommonEditor;

public class JsonEditor extends CommonEditor {
	protected JsonSourceViewerConfiguration configuration;
	

	public JsonEditor() {
		super();
		// create the document provider
		setDocumentProvider(new JsonDocumentProvider());
		
		//create the configuration of the editor
		configuration = new JsonSourceViewerConfiguration(this);
		setSourceViewerConfiguration(configuration);
		
	}

	@Override
	protected char[] getMatchingBrackets() {
		return jsonBrackets;
	};
	
	

}
