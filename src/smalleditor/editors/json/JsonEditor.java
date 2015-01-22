package smalleditor.editors.json;

import static smalleditor.utils.CharUtility.jsonBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonOutlinePage;

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
	
	protected CommonOutlinePage getOutlinePage(IDocument document) {
		JsonOutlinePage jsonContentOutlinePage = new JsonOutlinePage(document);
		return jsonContentOutlinePage;
	}

}
