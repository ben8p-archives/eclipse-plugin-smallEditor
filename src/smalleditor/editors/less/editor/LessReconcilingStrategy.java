package smalleditor.editors.less.editor;

import smalleditor.editors.common.editor.CommonEditor;
import smalleditor.editors.common.editor.CommonReconcilingStrategy;

public class LessReconcilingStrategy extends CommonReconcilingStrategy {
	
	public LessReconcilingStrategy(CommonEditor editor) {
		super(editor);
	}

	@Override
	protected LessDocumentTokenBuilder getDocumentTokenBuilder() {
		return new LessDocumentTokenBuilder(document);
	}
	

}
