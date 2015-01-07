package smalleditor.editors.javascript.editor;

import smalleditor.editors.common.editor.CommonContentAssistProcessor;
import smalleditor.util.CharUtility;

public class JavascriptContentAssistProcessor extends CommonContentAssistProcessor {
	public JavascriptContentAssistProcessor() {
		this.provider = new JavascriptContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.javascriptContentAssistTriggers;
	}
}