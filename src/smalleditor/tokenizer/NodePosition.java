package smalleditor.tokenizer;

import org.eclipse.jface.text.Position;


public class NodePosition extends Position {
	private DocumentNodeType type;
	private int level;
	public NodePosition(int position) {
		super(position);
	}
	public void setType(DocumentNodeType type) {
		this.type = type;
	}
	public DocumentNodeType getType() {
		return type;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
}
