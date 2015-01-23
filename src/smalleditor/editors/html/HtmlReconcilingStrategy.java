package smalleditor.editors.html;

import smalleditor.common.tokenizer.DocumentType;
import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonReconcilingStrategy;

public class HtmlReconcilingStrategy extends CommonReconcilingStrategy {

	public HtmlReconcilingStrategy(CommonEditor editor) {
		super(editor);
	}
	
	@Override
	protected HtmlDocumentTokenBuilder getDocumentTokenBuilder() {
		return (HtmlDocumentTokenBuilder) HtmlDocumentTokenBuilder.getDefault(DocumentType.HTML);
	}
	
}
