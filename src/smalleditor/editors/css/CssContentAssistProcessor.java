package smalleditor.editors.css;

import smalleditor.editors.common.ACommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class CssContentAssistProcessor extends ACommonContentAssistProcessor {
	public CssContentAssistProcessor() {
		this.provider = new CssContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.cssContentAssistTriggers;
	}
}