package smalleditor.editors.javascript;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonSourceViewerConfiguration;

public class JavascriptSourceViewerConfiguration extends ACommonSourceViewerConfiguration {
	
	public JavascriptSourceViewerConfiguration(JavascriptEditor editor) {
		super(editor);
		sourceConfiguration = JavascriptSourceConfiguration.getDefault();
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(ACommonEditor editor) {
		return new JavascriptReconcilingStrategy(this.editor);
	}

}