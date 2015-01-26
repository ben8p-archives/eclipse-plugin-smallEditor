package smalleditor.editors.less;

import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonReconcilingStrategy;
import smalleditor.editors.css.CssDocumentTokenBuilder;

public class LessReconcilingStrategy extends CommonReconcilingStrategy {
	
	public LessReconcilingStrategy(CommonEditor editor) {
		super(editor);
	}

	@Override
	protected CssDocumentTokenBuilder getDocumentTokenBuilder() {
		return (CssDocumentTokenBuilder) CssDocumentTokenBuilder.getDefault(DocumentType.LESS);
	}
	

}
