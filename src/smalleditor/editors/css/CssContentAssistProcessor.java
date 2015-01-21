package smalleditor.editors.css;

import smalleditor.editors.common.CommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class CssContentAssistProcessor extends CommonContentAssistProcessor {
	public CssContentAssistProcessor() {
		this.provider = new CssContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.cssContentAssistTriggers;
	}
}