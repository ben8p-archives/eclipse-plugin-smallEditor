package smalleditor.editors.json.editor;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.editor.CommonDocumentProvider;

public class JsonDocumentProvider extends CommonDocumentProvider {

	protected String[] getPartitions() {
		//Array of constant token types that will be color hilighted.
		String[] partition = { JsonPartitionScanner.JSON_COMMENT, JsonPartitionScanner.JSON_KEYSTRING, JsonPartitionScanner.JSON_STRING, JsonPartitionScanner.JSON_KEYWORD };
		return partition;
	}
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new JsonPartitionScanner();
	}
}