package smalleditor.editors.json;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.CommonDocumentProvider;

public class JsonDocumentProvider extends CommonDocumentProvider {

	public JsonDocumentProvider() {
		super();
		sourceConfiguration = JsonSourceConfiguration.getDefault();
	}
	
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new JsonPartitionScanner();
	}
}