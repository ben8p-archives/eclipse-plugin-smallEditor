package smalleditor.editors.json;

import static smalleditor.utils.CharUtility.jsonBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonOutlinePage;

public class JsonEditor extends ACommonEditor {
	protected JsonSourceViewerConfiguration configuration;
	private JsonOutlinePage outlinePage = null;

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
	
	protected ACommonOutlinePage getOutlinePage(IDocument document) {
		if(outlinePage == null) {
			outlinePage = new JsonOutlinePage();
			outlinePage.setDocument(document);
		}
		return outlinePage;
	}

}
