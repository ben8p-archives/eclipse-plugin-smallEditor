package smalleditor.editors.css;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.ACommonDocumentProvider;

public class CssDocumentProvider extends ACommonDocumentProvider {

	public CssDocumentProvider() {
		super();
		sourceConfiguration = CssSourceConfiguration.getDefault();
	}

	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new CssPartitionScanner();
	}
}