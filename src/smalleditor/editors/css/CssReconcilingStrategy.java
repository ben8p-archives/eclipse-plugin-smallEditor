package smalleditor.editors.css;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.WorkbenchJob;

import smalleditor.Activator;
import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonReconcilingStrategy;
import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.editors.common.parsing.ATaskPositionsBuilder;
import smalleditor.linters.css.CssLinterBuilder;
import smalleditor.nls.Messages;
import smalleditor.preferences.IPreferenceNames;
import smalleditor.tokenizer.DocumentType;

public class CssReconcilingStrategy extends ACommonReconcilingStrategy {
	protected CssLinterBuilder linterBuilder;
	
	public CssReconcilingStrategy(ACommonEditor editor) {
		super(editor);
		try {
			linterBuilder = new CssLinterBuilder();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected CssDocumentTokenBuilder getDocumentTokenBuilder() {
		return (CssDocumentTokenBuilder) CssDocumentTokenBuilder.getDefault(DocumentType.CSS);
	}
	
	protected void processReconcile() {
		super.processReconcile();
		
		Boolean useLinters = Activator.getDefault().getPreferenceStore().getBoolean(
				IPreferenceNames.P_LINT_CODE);
		if (useLinters == false) {
			return;
		}
		
		WorkbenchJob workbenchJob = new WorkbenchJob(Messages.getString("Lint.Job")) {//$NON-NLS-1$
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
			if(!(this.editor.getEditorInput() instanceof FileEditorInput)) { return; }
			IFile file = ((FileEditorInput) this.editor.getEditorInput())
					.getFile();

			linterBuilder.check(this.document, file);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected AFoldingPositionsBuilder getFoldingPositionsBuilder() {
		return null;
	}

	@Override
	protected ATaskPositionsBuilder getTaskPositionsBuilder() {
		return null;
	}

}
