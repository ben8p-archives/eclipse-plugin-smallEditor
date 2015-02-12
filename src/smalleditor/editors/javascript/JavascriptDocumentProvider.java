package smalleditor.editors.javascript;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import smalleditor.editors.common.ACommonDocumentProvider;

public class JavascriptDocumentProvider extends ACommonDocumentProvider {

	public JavascriptDocumentProvider() {
		super();
		sourceConfiguration = JavascriptSourceConfiguration.getDefault();
	}
	
	protected IPartitionTokenScanner getPartitionScanner() {
		return new JavascriptPartitionScanner();
	}
}