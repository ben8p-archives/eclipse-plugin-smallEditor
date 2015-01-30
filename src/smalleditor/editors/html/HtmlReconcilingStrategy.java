package smalleditor.editors.html;

import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.CommonReconcilingStrategy;

public class HtmlReconcilingStrategy extends CommonReconcilingStrategy {

	public HtmlReconcilingStrategy(ACommonEditor editor) {
		super(editor);
	}
	
	@Override
	protected HtmlDocumentTokenBuilder getDocumentTokenBuilder() {
		return (HtmlDocumentTokenBuilder) HtmlDocumentTokenBuilder.getDefault(DocumentType.HTML);
	}
	
}
