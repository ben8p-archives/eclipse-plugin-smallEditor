package smalleditor.editors.html.editor;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class HtmlPartitionScanner extends RuleBasedPartitionScanner {
	private HtmlSourceConfiguration sourceConfiguration = HtmlSourceConfiguration.getDefault();
	
	public HtmlPartitionScanner() {
		setPredicateRules(sourceConfiguration.getPartitioningRules());
	}
}