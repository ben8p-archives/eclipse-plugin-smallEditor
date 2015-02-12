package smalleditor.editors.common;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public abstract class ACommonDocumentProvider extends TextFileDocumentProvider {
	protected ACommonSourceConfiguration sourceConfiguration = null;
	public ACommonDocumentProvider() {
		super();
	}
	
	protected String[] getPartitions() {
		//Array of constant token types that will be color hilighted.
		return sourceConfiguration.getContentTypes();
	}
	protected IPartitionTokenScanner getPartitionScanner() {
		return null;
	}

	@Override
	protected void setUpSynchronization(FileInfo info) {
		super.setUpSynchronization(info);
		IDocument document= info.fTextFileBuffer.getDocument();
		setupDocument(document);
	}
	
	protected void setupDocument(IDocument document) {
		IDocumentPartitioner partitioner = new FastPartitioner(
				 getPartitionScanner(), getPartitions());
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
	}

	@Override
	protected IAnnotationModel createAnnotationModel(IFile file) {
		return new CommonAnnotationModelFactory().createAnnotationModel(file
				.getFullPath());
	}
	
}