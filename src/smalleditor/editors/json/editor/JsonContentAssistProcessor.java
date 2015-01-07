package smalleditor.editors.json.editor;

import smalleditor.editors.common.editor.CommonContentAssistProcessor;
import smalleditor.util.CharUtility;

public class JsonContentAssistProcessor extends CommonContentAssistProcessor {
	public JsonContentAssistProcessor() {
		this.provider = new JsonContentAssistProvider();
	}
	
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return CharUtility.jsonContentAssistTriggers;
	}
}