package smalleditor.editors.json;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.ACommonDocumentProvider;

public class JsonDocumentProvider extends ACommonDocumentProvider {

	public JsonDocumentProvider() {
		super();
		sourceConfiguration = JsonSourceConfiguration.getDefault();
	}
	
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new JsonPartitionScanner();
	}
}