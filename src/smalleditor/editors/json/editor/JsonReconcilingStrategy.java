package smalleditor.editors.json.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.part.FileEditorInput;

import smalleditor.editors.common.editor.CommonEditor;
import smalleditor.editors.common.editor.CommonReconcilingStrategy;
import smalleditor.linters.common.CommonLinterBuilder;
import smalleditor.linters.json.JsonLinterBuilder;

public class JsonReconcilingStrategy extends CommonReconcilingStrategy {
	protected CommonLinterBuilder linterBuilder;
	
	public JsonReconcilingStrategy(CommonEditor editor) {
		super(editor);
		try {
			linterBuilder = new JsonLinterBuilder();
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
