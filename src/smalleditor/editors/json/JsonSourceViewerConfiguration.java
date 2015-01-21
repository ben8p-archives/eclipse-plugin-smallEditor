package smalleditor.editors.json;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonSourceViewerConfiguration;

public class JsonSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	
	public JsonSourceViewerConfiguration(JsonEditor editor) {
		super(editor);
		sourceConfiguration = JsonSourceConfiguration.getDefault();
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new JsonReconcilingStrategy(this.editor);
	}

}