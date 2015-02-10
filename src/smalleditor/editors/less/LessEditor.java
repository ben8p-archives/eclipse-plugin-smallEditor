package smalleditor.editors.less;

import static smalleditor.utils.CharUtility.cssBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.ACommonOutlinePage;
import smalleditor.editors.css.CssEditor;

public class LessEditor extends CssEditor {
	protected LessSourceViewerConfiguration configuration;
	private LessOutlinePage outlinePage = null;
	
	public LessEditor() {
		super();
		// create the document provider
		setDocumentProvider(new LessDocumentProvider());
		
		//create the configuration of the editor
		configuration = new LessSourceViewerConfiguration(this);
		setSourceViewerConfiguration(configuration);
		
	}

	@Override
	protected char[] getMatchingBrackets() {
		return cssBrackets;
	};
	
	
	protected ACommonOutlinePage getOutlinePage(IDocument document) {
		if(outlinePage == null) {
			outlinePage = new LessOutlinePage(document);
		}
		return outlinePage;
	}
	

}
