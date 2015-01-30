package smalleditor.editors.common;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.WorkbenchJob;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentTokenBuilder;
import smalleditor.common.tokenizer.NodePosition;
import smalleditor.nls.Messages;

public class CommonReconcilingStrategy implements IReconcilingStrategy,
		IReconcilingStrategyExtension {
	protected IDocument document = null;
	protected CommonEditor editor;

	public CommonReconcilingStrategy(CommonEditor editor) {
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
	
	protected DocumentTokenBuilder getDocumentTokenBuilder() {
		return null;
	}
	
	protected void processReconcile() {
		if (document == null) {
			return;
		}
		
		WorkbenchJob workbenchJob = new WorkbenchJob(Messages.getString("Outline.Refresh")) { //$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor) {
		
				List<DocumentNode> nodes = getDocumentTokenBuilder().buildNodes(document);
				
				updateFoldingStructure(nodes);
				updateTaskAnnotation(nodes);
				updateOutline(nodes);
				return Status.OK_STATUS;
			}
	
		};
		workbenchJob.setPriority(WorkbenchJob.DECORATE);
		workbenchJob.schedule();
	}

	private void updateTaskAnnotation(List<DocumentNode> nodes) {
		List<Position> positions = new CommonTaskPositionsBuilder(nodes).buildTaskPositions();

		this.editor.updateTask(positions);
	}
	
	private void updateFoldingStructure(List<DocumentNode> nodes) {
		List<NodePosition> fPositions = new CommonFoldingPositionsBuilder(nodes).buildFoldingPositions();

		this.editor.updateFoldingStructure(fPositions);
	}
	private void updateOutline(List<DocumentNode> nodes) {
//		List<Position> fPositions = new CommonFoldingPositionsBuilder(nodes).buildFoldingPositions();
//
		this.editor.updateOutline(nodes);
	}
	

}
