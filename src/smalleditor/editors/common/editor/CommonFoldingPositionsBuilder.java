/**
 *
 */
package smalleditor.editors.common.editor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.Position;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;

/**
 * @author garner_m
 *
 */
public class CommonFoldingPositionsBuilder {

	private List<DocumentNode> nodes;

	public CommonFoldingPositionsBuilder(List<DocumentNode> nodes) {
		this.nodes = nodes;
	}

	public List<Position> buildFoldingPositions() {

		List<Position> positions = new LinkedList<Position>();
		List<Position> positionsStack = new LinkedList<Position>();
		
		int currentLine = -1;
		if (nodes != null) {
			for (DocumentNode node : nodes) {
				if (isNodeType(node, DocumentNodeType.OpenArray, DocumentNodeType.OpenObject)) {
					Position position = new Position(getStart(node));
					positionsStack.add(0, position);
					currentLine = node.getLine();
					continue;
				}
				
				if (isNodeType(node, DocumentNodeType.CloseArray, DocumentNodeType.CloseObject) && positionsStack.size() > 0) {
					Position position = positionsStack.remove(0);
					if(currentLine != node.getLine()) {
						position.setLength(getEnd(node) - position.getOffset());
						positions.add(position);
						currentLine = -1;
					}
					continue;
				}
			}
		}
		return positions;
	}

	private boolean isNodeType(DocumentNode node, DocumentNodeType ... types) {

		for (DocumentNodeType type : types) {
			if (node.getType() == type) {
				return true;
			}
		}

		return false;
	}

	public int getStart(DocumentNode node) {
		return node.getStart();
	}

	public int getEnd(DocumentNode node) {
		if (node == null) {
			return 0;
		}
		return node.getEnd();
	}
}
