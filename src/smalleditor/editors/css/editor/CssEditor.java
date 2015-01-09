package smalleditor.editors.css.editor;

import static smalleditor.util.CharUtility.cssBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.editor.CommonOutlinePage;
import smalleditor.editors.common.editor.CommonEditor;

public class CssEditor extends CommonEditor {
	protected CssSourceViewerConfiguration configuration;

	public CssEditor() {
		super();
		// create the document provider
		setDocumentProvider(new CssDocumentProvider());
		
		//create the configuration of the editor
		configuration = new CssSourceViewerConfiguration(this);
		setSourceViewerConfiguration(configuration);
		
	}

	@Override
	protected char[] getMatchingBrackets() {
		return cssBrackets;
	};
	
	
	protected CommonOutlinePage getOutlinePage(IDocument document) {
		CssOutlinePage javascriptContentOutlinePage = new CssOutlinePage(document);
		return javascriptContentOutlinePage;
	}
	

}
