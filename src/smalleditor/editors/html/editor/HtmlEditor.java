package smalleditor.editors.html.editor;

import smalleditor.editors.common.editor.CommonEditor;

public class HtmlEditor extends CommonEditor {
	protected HtmlSourceViewerConfiguration configuration;
	

	public HtmlEditor() {
		super();
		// create the document provider
		setDocumentProvider(new HtmlDocumentProvider());
		
		//create the configuration of the editor
		configuration = new HtmlSourceViewerConfiguration(this);
		setSourceViewerConfiguration(configuration);
		
	}

	@Override
	protected char[] getMatchingBrackets() {
		return null;
	};
	
	

}
