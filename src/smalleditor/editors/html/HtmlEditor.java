package smalleditor.editors.html;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonOutlinePage;

public class HtmlEditor extends ACommonEditor {
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
	}

	@Override
	protected ACommonOutlinePage getOutlinePage(IDocument document) {
		return null;
	};
	
	

}
