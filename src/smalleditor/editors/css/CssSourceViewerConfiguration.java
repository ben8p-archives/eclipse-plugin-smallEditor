package smalleditor.editors.css;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonSourceViewerConfiguration;

public class CssSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	
	public CssSourceViewerConfiguration(CssEditor editor) {
		super(editor);
		sourceConfiguration = CssSourceConfiguration.getDefault();
	}

	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new CssReconcilingStrategy(this.editor);
	}


}