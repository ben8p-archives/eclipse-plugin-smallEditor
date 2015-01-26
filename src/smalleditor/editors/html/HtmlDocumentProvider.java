package smalleditor.editors.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.common.ExtendedFastPartitioner;
import smalleditor.common.NullPartitionerSwitchStrategy;
import smalleditor.common.rules.CompositePartitionScanner;
import smalleditor.common.rules.NullSubPartitionScanner;
import smalleditor.editors.common.CommonDocumentProvider;

public class HtmlDocumentProvider extends CommonDocumentProvider {

	public HtmlDocumentProvider() {
		super();
		sourceConfiguration = HtmlSourceConfiguration.getDefault();
	}
	
	protected String[] getPartitions() {
		//Array of constant token types that will be color hilighted.
		String[] n = super.getPartitions();
		final List<String> list = new ArrayList<String>();
		Collections.addAll(list, n);
		list.remove(IDocument.DEFAULT_CONTENT_TYPE);
		n = list.toArray(new String[list.size()]);
		
		return n;
	}

	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new CompositePartitionScanner(HtmlSourceConfiguration
				.getDefault().createSubPartitionScanner(), new NullSubPartitionScanner(),
				new NullPartitionerSwitchStrategy());
	}
	@Override
	protected void setupDocument(Object element, IDocument document) {		
		IDocumentPartitioner partitioner = new ExtendedFastPartitioner(
				 getPartitionScanner(), getPartitions());
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
	}
}