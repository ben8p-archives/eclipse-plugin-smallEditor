package smalleditor.editors.html.editor;

import smalleditor.editors.common.editor.CommonContentAssistProcessor;
import smalleditor.util.CharUtility;

public class HtmlContentAssistProcessor extends CommonContentAssistProcessor {
	public HtmlContentAssistProcessor() {
		this.provider = new HtmlContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.htmlContentAssistTriggers;
	}
}