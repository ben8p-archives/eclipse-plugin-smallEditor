package smalleditor.editors.css.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.part.FileEditorInput;

import smalleditor.editors.common.editor.CommonEditor;
import smalleditor.editors.common.editor.CommonReconcilingStrategy;
import smalleditor.linters.css.CssLinterBuilder;

public class CssReconcilingStrategy extends CommonReconcilingStrategy {
	protected CssLinterBuilder linterBuilder;
	
	public CssReconcilingStrategy(CommonEditor editor) {
		super(editor);
		try {
			linterBuilder = new CssLinterBuilder();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected CssDocumentTokenBuilder getDocumentTokenBuilder() {
		return new CssDocumentTokenBuilder(document);
	}
	
	protected void processReconcile() {
		super.processReconcile();
		lintContent();
	}

	private void lintContent() {
		try {
			//System.out.println("linContent after reconcile");
			IFile file = ((FileEditorInput) this.editor.getEditorInput())
					.getFile();

			linterBuilder.check(this.document, file);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
