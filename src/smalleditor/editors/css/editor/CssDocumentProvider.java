package smalleditor.editors.css.editor;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.editor.CommonDocumentProvider;

public class CssDocumentProvider extends CommonDocumentProvider {

	protected String[] getPartitions() {
		//Array of constant token types that will be color hilighted.
		String[] partition = { CssPartitionScanner.CSS_COMMENT, CssPartitionScanner.CSS_CLASSNAME, CssPartitionScanner.CSS_STRING, CssPartitionScanner.CSS_COLOR, CssPartitionScanner.CSS_KEYWORD };
		return partition;
	}
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new CssPartitionScanner();
	}
}