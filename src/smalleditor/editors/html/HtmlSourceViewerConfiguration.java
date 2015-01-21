package smalleditor.editors.html;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonSourceViewerConfiguration;

public class HtmlSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	
	public HtmlSourceViewerConfiguration(HtmlEditor editor) {
		super(editor);
		sourceConfiguration = HtmlSourceConfiguration.getDefault();
	}

	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new HtmlReconcilingStrategy(this.editor);
	}


}