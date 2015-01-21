package smalleditor.editors.html;

import smalleditor.editors.common.CommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class HtmlContentAssistProcessor extends CommonContentAssistProcessor {
	public HtmlContentAssistProcessor() {
		this.provider = new HtmlContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.htmlContentAssistTriggers;
	}
}