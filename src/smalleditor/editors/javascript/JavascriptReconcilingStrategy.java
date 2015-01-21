package smalleditor.editors.javascript;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.part.FileEditorInput;

import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonReconcilingStrategy;
import smalleditor.linters.javascript.JavascriptLinterBuilder;

public class JavascriptReconcilingStrategy extends CommonReconcilingStrategy {
	protected JavascriptLinterBuilder linterBuilder;

	public JavascriptReconcilingStrategy(CommonEditor editor) {
		super(editor);
		try {
			linterBuilder = new JavascriptLinterBuilder();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected JavascriptDocumentTokenBuilder getDocumentTokenBuilder() {
		return new JavascriptDocumentTokenBuilder(document);
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
