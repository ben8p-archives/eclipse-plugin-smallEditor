package smalleditor.editors.css;

import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.DocumentTokenBuilder;

public class CssDocumentTokenBuilder extends DocumentTokenBuilder {
	@SuppressWarnings("nls")
	public CssDocumentTokenBuilder() {
		super();
		this.setElements(
			new String[] {"todo", "fixme", "\n", "{", "}", ",", ":", ";", "/*", "*/"},
			new DocumentNodeType[] {DocumentNodeType.Todo, DocumentNodeType.Fixme, DocumentNodeType.NewLine, DocumentNodeType.OpenObject, DocumentNodeType.CloseObject, DocumentNodeType.Comma, DocumentNodeType.Colon, null, DocumentNodeType.OpenMultilineComment, DocumentNodeType.CloseMultilineComment}
		);
	}
	
	
}
