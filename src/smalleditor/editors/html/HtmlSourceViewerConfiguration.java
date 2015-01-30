package smalleditor.editors.html;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonSourceViewerConfiguration;

public class HtmlSourceViewerConfiguration extends ACommonSourceViewerConfiguration {
	
	public HtmlSourceViewerConfiguration(HtmlEditor editor) {
		super(editor);
		sourceConfiguration = HtmlSourceConfiguration.getDefault();
	}

	@Override
	protected IReconcilingStrategy getReconcilingStrategy(ACommonEditor editor) {
		return new HtmlReconcilingStrategy(this.editor);
	}


}