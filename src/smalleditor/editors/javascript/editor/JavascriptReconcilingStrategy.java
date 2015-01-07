package smalleditor.editors.javascript.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.part.FileEditorInput;

import smalleditor.editors.common.editor.CommonEditor;
import smalleditor.editors.common.editor.CommonReconcilingStrategy;
import smalleditor.linters.javascript.Builder;

public class JavascriptReconcilingStrategy extends CommonReconcilingStrategy {
	protected Builder linterBuilder;

	public JavascriptReconcilingStrategy(CommonEditor editor) {
		super(editor);
		try {
			linterBuilder = new Builder();
		} catch (CoreException e) {
			e.printStackTrace();
		}
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
