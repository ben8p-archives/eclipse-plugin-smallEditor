package smalleditor.editors.javascript;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class JavascriptPartitionScanner extends RuleBasedPartitionScanner {
	private JavascriptSourceConfiguration sourceConfiguration = JavascriptSourceConfiguration.getDefault();
	

	/**
	 * Creates a new JSPartitionScanner object.
	 */
	public JavascriptPartitionScanner() {
		setPredicateRules(sourceConfiguration.getPartitioningRules());
	}

}