package smalleditor.editors.javascript.editor;

import static smalleditor.util.CharUtility.javascriptBrackets;
import smalleditor.editors.common.editor.CommonEditor;

public class JavascriptEditor extends CommonEditor {
	protected JavascriptSourceViewerConfiguration configuration;
	

	public JavascriptEditor() {
		super();
		// create the document provider
		setDocumentProvider(new JavascriptDocumentProvider());
		
		//create the configuration of the editor
		configuration = new JavascriptSourceViewerConfiguration(this);
		setSourceViewerConfiguration(configuration);
		
	}

	@Override
	protected char[] getMatchingBrackets() {
		return javascriptBrackets;
	};
	
	

}