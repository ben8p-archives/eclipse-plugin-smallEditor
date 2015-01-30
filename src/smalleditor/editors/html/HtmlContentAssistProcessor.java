package smalleditor.editors.html;

import smalleditor.editors.common.ACommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class HtmlContentAssistProcessor extends ACommonContentAssistProcessor {
	public HtmlContentAssistProcessor() {
		this.provider = new HtmlContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.htmlContentAssistTriggers;
	}
}