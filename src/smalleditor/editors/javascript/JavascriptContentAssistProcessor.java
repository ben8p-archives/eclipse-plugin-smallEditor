package smalleditor.editors.javascript;

import smalleditor.editors.common.ACommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class JavascriptContentAssistProcessor extends ACommonContentAssistProcessor {
	public JavascriptContentAssistProcessor() {
		this.provider = new JavascriptContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.javascriptContentAssistTriggers;
	}
}