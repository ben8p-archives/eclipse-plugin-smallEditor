package smalleditor.editors.css;

import static smalleditor.utils.CharUtility.cssBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonOutlinePage;

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
		CssOutlinePage cssContentOutlinePage = new CssOutlinePage(document);
		return cssContentOutlinePage;
	}
	

}
