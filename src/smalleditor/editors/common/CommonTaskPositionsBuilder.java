/**
 *
 */
package smalleditor.editors.common;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.Position;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;

/**
 * @author garner_m
 *
 */
public class CommonTaskPositionsBuilder {

	private List<DocumentNode> nodes;

	public CommonTaskPositionsBuilder(List<DocumentNode> nodes) {
		this.nodes = nodes;
	}

	public List<Position> buildTaskPositions() {

		List<Position> positions = new LinkedList<Position>();
		
		if (nodes != null) {
			for (DocumentNode node : nodes) {
				if (isNodeType(node, DocumentNodeType.Todo, DocumentNodeType.Fixme)) {
					Position position = new Position(getStart(node));
					position.setLength(getEnd(node) - position.getOffset());
					positions.add(position);
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
