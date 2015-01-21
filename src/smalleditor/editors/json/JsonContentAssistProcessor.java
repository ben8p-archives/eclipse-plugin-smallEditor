package smalleditor.editors.json;

import smalleditor.editors.common.CommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class JsonContentAssistProcessor extends CommonContentAssistProcessor {
	public JsonContentAssistProcessor() {
		this.provider = new JsonContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.jsonContentAssistTriggers;
	}
}