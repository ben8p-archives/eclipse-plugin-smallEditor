package smalleditor.editors.common.editor;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;

import smalleditor.editors.common.model.Node;
import smalleditor.editors.common.model.NodeBuilder;

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
	
	protected void processReconcile() {
		if (document == null) {
			return;
		}
		
		final List<Node> nodes = new NodeBuilder(document).buildNodes();
		
		updateFoldingStructure(nodes);
		updateTaskAnnotation(nodes);
	}

	private void updateTaskAnnotation(List<Node> nodes) {
		final List<Position> positions = new CommonTaskPositionsBuilder(nodes).buildTaskPositions();

		this.editor.updateTask(positions);
	}
	
	private void updateFoldingStructure(List<Node> nodes) {
		final List<Position> fPositions = new CommonFoldingPositionsBuilder(nodes).buildFoldingPositions();

		this.editor.updateFoldingStructure(fPositions);
	}

}
