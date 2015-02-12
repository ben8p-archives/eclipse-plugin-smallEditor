package smalleditor.tokenizer;


import org.eclipse.jface.text.Position;


public class DocumentNode {

	private DocumentNodeType type;

	private Position position;
	
	private int line;

	private String value;

	public DocumentNode(DocumentNodeType type) {
		super();
		this.type = type;
	}

	public DocumentNodeType getType() {
		return type;
	}

	public Position getPosition() {
		return position;
	}
	
	public int getLine() {
		return line;
	}

	public int getStart() {
		if (position != null && !position.isDeleted) {
			return position.getOffset();
		}
		return -1;
	}

	public void setStart(int start) {
		position = new Position(start);
	}

	public int getLength() {
		if (position != null && !position.isDeleted) {
			return position.getLength();
		}
		return -1;
	}

	public void setLength(int length) {
		position.setLength(length);
	}
	
	public void setLine(int lineNb) {
		line = lineNb;
	}

	public void setPosition(int start, int length) {
		position = new Position(start, length);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null) {
			value.replaceAll("\\\"", "\""); //$NON-NLS-1$ //$NON-NLS-2$
			this.value = value;
		}
	}

	public int getEnd() {

		if (position != null && !position.isDeleted) {
			return position.getOffset() + position.getLength();
		}
		return -1;
	}

	@Override
	public String toString() {
		String toString = "[[type: " + type.toString(); //$NON-NLS-1$
		toString += ", offset: " + position.offset + ", length: " + position.length; //$NON-NLS-1$ //$NON-NLS-2$
		toString += ", line: " + line + ", value: " + value + "]]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return toString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentNode other = (DocumentNode) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
