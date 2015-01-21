package smalleditor.editors.javascript;

import smalleditor.editors.common.CommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class JavascriptContentAssistProcessor extends CommonContentAssistProcessor {
	public JavascriptContentAssistProcessor() {
		this.provider = new JavascriptContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.javascriptContentAssistTriggers;
	}
}