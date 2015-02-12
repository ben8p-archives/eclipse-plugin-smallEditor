package smalleditor.editors.css;

import static smalleditor.utils.CharUtility.cssBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonOutlinePage;

public class CssEditor extends ACommonEditor {
	protected CssSourceViewerConfiguration configuration;
	private CssOutlinePage outlinePage = null;
	
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
	
	
	protected ACommonOutlinePage getOutlinePage(IDocument document) {
		if(outlinePage == null) {
			outlinePage = new CssOutlinePage();
			outlinePage.setDocument(document);
		}
		return outlinePage;
	}
	

}
