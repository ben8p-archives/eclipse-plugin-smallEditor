package smalleditor.editors.css;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonSourceViewerConfiguration;

public class CssSourceViewerConfiguration extends ACommonSourceViewerConfiguration {
	
	public CssSourceViewerConfiguration(CssEditor editor) {
		super(editor);
		sourceConfiguration = CssSourceConfiguration.getDefault();
	}

	@Override
	protected IReconcilingStrategy getReconcilingStrategy(ACommonEditor editor) {
		return new CssReconcilingStrategy(this.editor);
	}


}