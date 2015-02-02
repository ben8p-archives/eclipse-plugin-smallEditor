package smalleditor.editors.css;

import smalleditor.editors.common.ACommonContentAssistProcessor;

public class CssContentAssistProcessor extends ACommonContentAssistProcessor {
	public CssContentAssistProcessor() {
		this.provider = new CssContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null; //CharUtility.cssContentAssistTriggers;
	}
}