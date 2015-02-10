/**
 *
 */
package smalleditor.editors.common;

import java.util.LinkedList;
import java.util.List;


import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentNodeType;
import smalleditor.common.tokenizer.NodePosition;

/**
 * @author garner_m
 *
 */
public class CommonFoldingPositionsBuilder {

	private List<DocumentNode> nodes;

	public CommonFoldingPositionsBuilder(List<DocumentNode> nodes) {
		this.nodes = nodes;
	}

	public List<NodePosition> buildFoldingPositions() {

		List<NodePosition> positions = new LinkedList<NodePosition>();
		List<NodePosition> positionsStack = new LinkedList<NodePosition>();
		List<NodePosition> linesStack = new LinkedList<NodePosition>();
		int level = 0;
		NodePosition adjustablePosition = null;
		if (nodes != null) {
			for (DocumentNode node : nodes) {
				if (isNodeType(node, DocumentNodeType.OpenArray, DocumentNodeType.OpenObject, DocumentNodeType.OpenFunction)) {
					NodePosition position = new NodePosition(getStart(node));
					positionsStack.add(0, position);
					linesStack.add(0, new NodePosition(node.getLine()));
					position.setType(node.getType());
					adjustablePosition = null;
					level++;
				} else if (isNodeType(node, DocumentNodeType.CloseArray, DocumentNodeType.CloseObject) && positionsStack.size() > 0) {
					NodePosition position = positionsStack.remove(0);
					NodePosition line = linesStack.remove(0);
					if(line.getOffset() != node.getLine()) {
						position.setLength(getEnd(node) - position.getOffset());
						position.setLevel(level);
						positions.add(position);
						adjustablePosition = position;
					}
					level--;
				} else if (isNodeType(node, DocumentNodeType.NewLine) && adjustablePosition != null) {
					adjustablePosition.setLength(getEnd(node) - adjustablePosition.getOffset());
					adjustablePosition = null;
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
