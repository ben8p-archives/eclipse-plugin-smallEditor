package smalleditor.editors.html;

import smalleditor.editors.common.CommonEditor;

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
