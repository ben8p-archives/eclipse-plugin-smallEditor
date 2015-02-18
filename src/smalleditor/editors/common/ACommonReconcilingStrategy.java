package smalleditor.editors.common;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.WorkbenchJob;

import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.editors.common.parsing.ATaskPositionsBuilder;
import smalleditor.editors.common.parsing.FoldingPositionsNodeBuilder;
import smalleditor.editors.common.parsing.TaskPositionsNodeBuilder;
import smalleditor.nls.Messages;
import smalleditor.tokenizer.DocumentNode;
import smalleditor.tokenizer.DocumentTokenBuilder;

public abstract class ACommonReconcilingStrategy implements IReconcilingStrategy,
		IReconcilingStrategyExtension {
	protected IDocument document = null;
	protected ACommonEditor editor;

	public ACommonReconcilingStrategy(ACommonEditor editor) {
		super();
		this.editor = editor;
	}

	@Override
	public void initialReconcile() {
		reconcile();
	}

	@Override
	public void setProgressMonitor(IProgressMonitor arg0) {
		
	}

	@Override
	public void reconcile(IRegion arg0) {
		reconcile();
	}

	@Override
	public void reconcile(DirtyRegion arg0, IRegion arg1) {
		reconcile();
	}

	@Override
	public void setDocument(IDocument doc) {
		document = doc;
	}

	private void reconcile() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				processReconcile();
			}
		});
	}
	
	protected abstract DocumentTokenBuilder getDocumentTokenBuilder();
	protected abstract AFoldingPositionsBuilder getFoldingPositionsBuilder();
	protected abstract ATaskPositionsBuilder getTaskPositionsBuilder();

	protected void processReconcile() {
		if (document == null) {
			return;
		}
		
		WorkbenchJob workbenchJob = new WorkbenchJob(Messages.getString("View.Refresh")) { //$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor) {
//				System.out.println(document.get());
				DocumentTokenBuilder builder = getDocumentTokenBuilder();
				//using a custom made simple node parser with easy rules
				if(builder != null) {
					List<DocumentNode> nodes = builder.buildNodes(document);
					editor.updateTask(new TaskPositionsNodeBuilder(nodes).buildTaskPositions());
					editor.updateFoldingStructure(new FoldingPositionsNodeBuilder(nodes).buildFoldingPositions());
					
				} else {
					//using a beaver parser for complex language
					ATaskPositionsBuilder taskBuilder = getTaskPositionsBuilder();
					if(taskBuilder != null) {
						editor.updateTask(taskBuilder.buildPositions());
					}
					AFoldingPositionsBuilder foldingBuilder = getFoldingPositionsBuilder();
					if(foldingBuilder != null) {
						editor.updateFoldingStructure(foldingBuilder.buildPositions());
					}
				}
				editor.updateOutline();
				
				return Status.OK_STATUS;
			}
	
		};
		workbenchJob.setPriority(WorkbenchJob.DECORATE);
		workbenchJob.schedule();
	}
}
