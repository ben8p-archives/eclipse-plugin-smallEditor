package smalleditor.editors.json;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.WorkbenchJob;

import smalleditor.Activator;
import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonReconcilingStrategy;
import smalleditor.linters.common.CommonLinterBuilder;
import smalleditor.linters.json.JsonLinterBuilder;
import smalleditor.preferences.PreferenceNames;

public class JsonReconcilingStrategy extends CommonReconcilingStrategy {
	protected CommonLinterBuilder linterBuilder;
	
	@Override
	protected JsonDocumentTokenBuilder getDocumentTokenBuilder() {
		return (JsonDocumentTokenBuilder) JsonDocumentTokenBuilder.getDefault(DocumentType.JSON);
	}
	
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
		
		Boolean useLinters = Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceNames.P_LINT_CODE);
		if (useLinters == false) {
			return;
		}
		
		WorkbenchJob workbenchJob = new WorkbenchJob("Lint content") {//$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor) {
				lintContent();
				return Status.OK_STATUS;
			}
		};
		workbenchJob.setPriority(WorkbenchJob.DECORATE);
		workbenchJob.schedule();
		
//		Display.getDefault().asyncExec(new Runnable() {
//			public void run() {
//				lintContent();
//			}
//		});
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
