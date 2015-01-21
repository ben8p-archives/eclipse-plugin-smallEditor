package smalleditor.editors.javascript;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import smalleditor.editors.common.CommonDocumentProvider;

public class JavascriptDocumentProvider extends CommonDocumentProvider {

	public JavascriptDocumentProvider() {
		super();
		sourceConfiguration = JavascriptSourceConfiguration.getDefault();
	}
	
	protected RuleBasedPartitionScanner getPartitionScanner() {
		return new JavascriptPartitionScanner();
	}
}