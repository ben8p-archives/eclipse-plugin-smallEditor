package smalleditor.tokenizer;

import org.eclipse.jface.text.Position;


public class NodePosition extends Position {
	private DocumentNodeType type = null;
	private int level = 0;
	private int hash = 0;
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
	public void setHashCode(int value) {
		this.hash = value;
	}
	public int getHashCode() {
		return hash;
	}
}
