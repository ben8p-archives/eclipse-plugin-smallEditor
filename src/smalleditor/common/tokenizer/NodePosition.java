package smalleditor.common.tokenizer;

import org.eclipse.jface.text.Position;


public class NodePosition extends Position {
	private DocumentNodeType type;
	public NodePosition(int position) {
		super(position);
	}
	public void setType(DocumentNodeType type) {
		this.type = type;
	}
	public DocumentNodeType getType() {
		return type;
	}
}
