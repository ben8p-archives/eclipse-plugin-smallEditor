package smalleditor.editors.json;

import smalleditor.editors.common.ACommonContentAssistProcessor;

public class JsonContentAssistProcessor extends ACommonContentAssistProcessor {
	public JsonContentAssistProcessor() {
		this.provider = new JsonContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null; //CharUtility.jsonContentAssistTriggers;
	}
}