package smalleditor.editors.html.editor;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.editor.CommonDocumentProvider;

public class HtmlDocumentProvider extends CommonDocumentProvider {

	protected String[] getPartitions() {
		//Array of constant token types that will be color hilighted.
		String[] partition = { HtmlPartitionScanner.HTML_COMMENT, HtmlPartitionScanner.HTML_TAG, HtmlPartitionScanner.HTML_STRING, HtmlPartitionScanner.HTML_ATTRIBUTE };
		return partition;
	}
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new HtmlPartitionScanner();
	}
}