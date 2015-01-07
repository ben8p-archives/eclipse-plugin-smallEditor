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
public class CommonTaskPositionsBuilder {

	private List<Node> nodes;

	public CommonTaskPositionsBuilder(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Position> buildTaskPositions() {

		List<Position> positions = new LinkedList<Position>();
		
		if (nodes != null) {
			for (Node node : nodes) {
				if (isNodeType(node, Type.Todo, Type.Fixme)) {
					Position position = new Position(getStart(node));
					position.setLength(getEnd(node) - position.getOffset());
					positions.add(position);
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
