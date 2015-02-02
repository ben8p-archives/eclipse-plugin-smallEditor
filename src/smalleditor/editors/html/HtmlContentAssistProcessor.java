package smalleditor.editors.html;

import smalleditor.editors.common.ACommonContentAssistProcessor;

public class HtmlContentAssistProcessor extends ACommonContentAssistProcessor {
	public HtmlContentAssistProcessor() {
		this.provider = new HtmlContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null; //CharUtility.htmlContentAssistTriggers;
	}
}