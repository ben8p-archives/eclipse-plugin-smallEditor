package smalleditor.editors.css;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;


public class CssPartitionScanner extends RuleBasedPartitionScanner {
	private CssSourceConfiguration sourceConfiguration = CssSourceConfiguration.getDefault();
	
	public CssPartitionScanner() {
		setPredicateRules(sourceConfiguration.getPartitioningRules());
	}

}