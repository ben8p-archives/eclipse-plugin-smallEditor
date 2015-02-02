package smalleditor.editors.javascript;

import smalleditor.editors.common.ACommonContentAssistProcessor;

public class JavascriptContentAssistProcessor extends ACommonContentAssistProcessor {
	public JavascriptContentAssistProcessor() {
		this.provider = new JavascriptContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null; //CharUtility.javascriptContentAssistTriggers;
	}
}