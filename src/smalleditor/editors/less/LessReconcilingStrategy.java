package smalleditor.editors.less;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonReconcilingStrategy;
import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.editors.common.parsing.ATaskPositionsBuilder;
import smalleditor.editors.css.CssDocumentTokenBuilder;
import smalleditor.tokenizer.DocumentType;

public class LessReconcilingStrategy extends ACommonReconcilingStrategy {
	
	public LessReconcilingStrategy(ACommonEditor editor) {
		super(editor);
	}

	@Override
	protected CssDocumentTokenBuilder getDocumentTokenBuilder() {
		return (CssDocumentTokenBuilder) CssDocumentTokenBuilder.getDefault(DocumentType.LESS);
	}

	@Override
	protected AFoldingPositionsBuilder getFoldingPositionsBuilder() {
		return null;
	}

	@Override
	protected ATaskPositionsBuilder getTaskPositionsBuilder() {
		return null;
	}


}
