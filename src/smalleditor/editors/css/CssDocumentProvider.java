package smalleditor.editors.css;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.CommonDocumentProvider;

public class CssDocumentProvider extends CommonDocumentProvider {

	public CssDocumentProvider() {
		super();
		sourceConfiguration = CssSourceConfiguration.getDefault();
	}

	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new CssPartitionScanner();
	}
}