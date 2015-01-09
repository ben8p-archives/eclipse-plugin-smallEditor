package smalleditor.editors.css.editor;

import smalleditor.editors.common.editor.CommonContentAssistProcessor;
import smalleditor.util.CharUtility;

public class CssContentAssistProcessor extends CommonContentAssistProcessor {
	public CssContentAssistProcessor() {
		this.provider = new CssContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.cssContentAssistTriggers;
	}
}