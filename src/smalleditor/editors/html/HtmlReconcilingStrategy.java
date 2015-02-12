package smalleditor.editors.html;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonReconcilingStrategy;
import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.editors.common.parsing.ATaskPositionsBuilder;
import smalleditor.tokenizer.DocumentType;

public class HtmlReconcilingStrategy extends ACommonReconcilingStrategy {

	public HtmlReconcilingStrategy(ACommonEditor editor) {
		super(editor);
	}
	
	@Override
	protected HtmlDocumentTokenBuilder getDocumentTokenBuilder() {
		return (HtmlDocumentTokenBuilder) HtmlDocumentTokenBuilder.getDefault(DocumentType.HTML);
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
