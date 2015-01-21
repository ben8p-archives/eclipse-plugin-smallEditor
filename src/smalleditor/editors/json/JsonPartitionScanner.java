package smalleditor.editors.json;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class JsonPartitionScanner extends RuleBasedPartitionScanner {
	private JsonSourceConfiguration sourceConfiguration = JsonSourceConfiguration.getDefault();
	
	public JsonPartitionScanner() {
		setPredicateRules(sourceConfiguration.getPartitioningRules());
	}
}