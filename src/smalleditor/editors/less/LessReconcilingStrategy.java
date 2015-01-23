package smalleditor.editors.less;

import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonReconcilingStrategy;

public class LessReconcilingStrategy extends CommonReconcilingStrategy {
	
	public LessReconcilingStrategy(CommonEditor editor) {
		super(editor);
	}

	@Override
	protected LessDocumentTokenBuilder getDocumentTokenBuilder() {
		return (LessDocumentTokenBuilder) LessDocumentTokenBuilder.getDefault(DocumentType.LESS);
	}
	

}
