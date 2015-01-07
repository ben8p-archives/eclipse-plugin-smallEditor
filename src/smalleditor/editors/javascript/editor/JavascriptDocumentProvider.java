package smalleditor.editors.javascript.editor;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.editor.CommonDocumentProvider;

public class JavascriptDocumentProvider extends CommonDocumentProvider {

	protected String[] getPartitions() {
		//Array of constant token types that will be color hilighted.
		String[] partition = { JavascriptPartitionScanner.JS_COMMENT, JavascriptPartitionScanner.JS_STRING, JavascriptPartitionScanner.JS_KEYWORD };
		return partition;
	}
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new JavascriptPartitionScanner();
	}
}