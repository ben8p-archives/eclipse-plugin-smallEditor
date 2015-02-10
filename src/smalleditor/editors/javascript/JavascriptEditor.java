package smalleditor.editors.javascript;

import static smalleditor.utils.CharUtility.javascriptBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonOutlinePage;

public class JavascriptEditor extends ACommonEditor {
	protected JavascriptSourceViewerConfiguration configuration;
	private JavascriptOutlinePage outlinePage = null;

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
	
	
	protected ACommonOutlinePage getOutlinePage(IDocument document) {
		if(outlinePage == null) {
			outlinePage = new JavascriptOutlinePage(document);
		}
		return outlinePage;
	}
	

}
