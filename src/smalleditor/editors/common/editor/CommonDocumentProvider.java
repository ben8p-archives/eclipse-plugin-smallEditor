package smalleditor.editors.common.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class CommonDocumentProvider extends FileDocumentProvider {

	public CommonDocumentProvider() {
		super();
	}
	
	protected String[] getPartitions() {
		//Array of constant token types that will be color hilighted.
		String[] partition = { };
		return partition;
	}
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return null;
	}
	
	protected void setupDocument(Object element, IDocument document) {
		IDocumentPartitioner partitioner = new FastPartitioner(
				 getPartitionScanner(), getPartitions());
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
	}
}