package smalleditor.editors.less;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonReconcilingStrategy;
import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.editors.common.parsing.ATaskPositionsBuilder;
import smalleditor.editors.css.parsing.CssFoldingPositionsBuilder;
import smalleditor.editors.css.parsing.CssTaskPositionsBuilder;
import smalleditor.tokenizer.DocumentTokenBuilder;

public class LessReconcilingStrategy extends ACommonReconcilingStrategy {
	
	public LessReconcilingStrategy(ACommonEditor editor) {
		super(editor);
	}
	
	@Override
	protected AFoldingPositionsBuilder getFoldingPositionsBuilder() {
		return new CssFoldingPositionsBuilder(this.document);
	}
	@Override
	protected ATaskPositionsBuilder getTaskPositionsBuilder() {
		return new CssTaskPositionsBuilder(this.document);
	}
	@Override
	protected DocumentTokenBuilder getDocumentTokenBuilder() {
		return null;
	}


}
