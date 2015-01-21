package smalleditor.editors.javascript;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonSourceViewerConfiguration;

public class JavascriptSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	
	public JavascriptSourceViewerConfiguration(JavascriptEditor editor) {
		super(editor);
		sourceConfiguration = JavascriptSourceConfiguration.getDefault();
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new JavascriptReconcilingStrategy(this.editor);
	}

}