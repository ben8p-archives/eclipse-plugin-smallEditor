package smalleditor.editors.less.editor;

import static smalleditor.util.CharUtility.cssBrackets;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.editor.CommonOutlinePage;
import smalleditor.editors.css.editor.CssEditor;

public class LessEditor extends CssEditor {
	protected LessSourceViewerConfiguration configuration;

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
	
	
	protected CommonOutlinePage getOutlinePage(IDocument document) {
		LessOutlinePage javascriptContentOutlinePage = new LessOutlinePage(document);
		return javascriptContentOutlinePage;
	}
	

}
