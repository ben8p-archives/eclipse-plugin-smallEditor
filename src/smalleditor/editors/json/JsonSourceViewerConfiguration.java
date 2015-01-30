package smalleditor.editors.json;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonSourceViewerConfiguration;

public class JsonSourceViewerConfiguration extends ACommonSourceViewerConfiguration {
	
	public JsonSourceViewerConfiguration(JsonEditor editor) {
		super(editor);
		sourceConfiguration = JsonSourceConfiguration.getDefault();
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(ACommonEditor editor) {
		return new JsonReconcilingStrategy(this.editor);
	}

}