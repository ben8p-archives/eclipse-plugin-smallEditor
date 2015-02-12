/**
 *
 */
package smalleditor.editors.common.parsing;

import java.util.LinkedList;
import java.util.List;

import smalleditor.tokenizer.DocumentNode;
import smalleditor.tokenizer.DocumentNodeType;
import smalleditor.tokenizer.NodePosition;

/**
 * @author garner_m
 *
 */
public class TaskPositionsNodeBuilder {

	private List<DocumentNode> nodes;

	public TaskPositionsNodeBuilder(List<DocumentNode> nodes) {
		this.nodes = nodes;
	}

	public List<NodePosition> buildTaskPositions() {

		List<NodePosition> positions = new LinkedList<NodePosition>();
		
		if (nodes != null) {
			for (DocumentNode node : nodes) {
				if (isNodeType(node, DocumentNodeType.Todo, DocumentNodeType.Fixme)) {
					NodePosition position = new NodePosition(getStart(node));
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
