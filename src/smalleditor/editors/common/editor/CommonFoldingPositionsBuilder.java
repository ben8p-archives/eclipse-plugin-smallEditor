/**
 *
 */
package smalleditor.editors.common.editor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.Position;

import smalleditor.editors.common.model.Node;
import smalleditor.editors.common.model.Type;

/**
 * @author garner_m
 *
 */
public class CommonFoldingPositionsBuilder {

	private List<Node> nodes;

	public CommonFoldingPositionsBuilder(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Position> buildFoldingPositions() {

		List<Position> positions = new LinkedList<Position>();
		List<Position> positionsStack = new LinkedList<Position>();
		
		Boolean newLines = false;
		if (nodes != null) {
			for (Node node : nodes) {
				if (isNodeType(node, Type.OpenArray, Type.OpenObject)) {
					Position position = new Position(getStart(node));
					positionsStack.add(0, position);
					newLines = false;
					continue;
				}
				if (positionsStack.size() > 0 && isNodeType(node, Type.NewLine)) {
					newLines = true;
					continue;
				}
				
				if (isNodeType(node, Type.CloseArray, Type.CloseObject) && positionsStack.size() > 0) {
					Position position = positionsStack.remove(0);
					if(newLines) {
						position.setLength(getEnd(node) - position.getOffset());
						positions.add(position);
						newLines = false;
					}
					continue;
				}
			}
		}
		return positions;
	}

	private boolean isNodeType(Node node, Type ... types) {

		for (Type type : types) {
			if (node.getType() == type) {
				return true;
			}
		}

		return false;
	}

	public int getStart(Node node) {
		return node.getStart();
	}

	public int getEnd(Node node) {
		if (node == null) {
			return 0;
		}
		return node.getEnd();
	}
}
