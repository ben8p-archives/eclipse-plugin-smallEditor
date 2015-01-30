package smalleditor.editors.json;

import smalleditor.editors.common.ACommonContentAssistProcessor;
import smalleditor.utils.CharUtility;

public class JsonContentAssistProcessor extends ACommonContentAssistProcessor {
	public JsonContentAssistProcessor() {
		this.provider = new JsonContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.jsonContentAssistTriggers;
	}
}