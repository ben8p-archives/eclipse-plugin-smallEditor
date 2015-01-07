package smalleditor.editors.common.model;

import static smalleditor.util.CharUtility.eof;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.model.reader.DocumentReader;
import smalleditor.editors.common.model.reader.Reader;
import smalleditor.util.CharUtility;

public class NodeBuilder {

	private IDocument fDocument;

	private Reader reader;

	private List<Node> nodes;

	public NodeBuilder(IDocument document) {
		super();
		fDocument = document;
	}

	public List<Node> buildNodes(){

		nodes = new LinkedList<Node>();
		reader = new DocumentReader(fDocument);

		try {
			char ch;
			do {
				ch = reader.getNextClean();
				Node node = buildNode(ch);
				if (node != null) {
					nodes.add(node);
				}
			}
			while (ch != eof);
		} catch (Exception e) {
			return null;
		}

		parseNodes(nodes);

		return nodes;
	}

	private Node buildNode(char ch) {
		char previous = reader.getPrevious();
		if (CharUtility.isBlock(ch) || CharUtility.isNewLine(ch) || CharUtility.isOneLineComment(ch, previous)) {
			Node node = new Node(getCharType(ch, previous));
			node.setPosition(reader.getPosition(), 1);
			node.setValue(String.valueOf(ch));
			return node;
		}

		return null;
	}

	private void parseNodes(List<Node> nodes) {

		int i = 0;
		while(i < nodes.size() - 1) {
			Node node = nodes.get(i);
			Node node2 = nodes.get(i+1);

			compareNodes(node, node2, i);
			i++;
		}
	}

	private void compareNodes(Node node, Node node2, int index) {

		if (node.getType() == Type.OpenObject) {
			compareOpenObject(node, node2, index);
		}
		
		if (node.getType() == Type.NewLine) {
			compareNewLine(node, node2, index);
		}
		
		if (node.getType() == Type.OneLineComment) {
			compareOneLineComment(node, node2, index);
		}

		if (node.getType() == Type.Quote) {
			compareQuote(node, node2, index);
		}

		if (node.getType() == Type.OpenArray) {
			compareOpenArray(node, node2, index);
		}

		if (node.getType() == Type.Colon) {
			compareColon(node, node2, index);
		}

		if (node.getType() == Type.Comma) {
			compareComma(node, node2, index);
		}

		if (node.getType() == Type.CloseArray) {
			compareCloseArray(node, node2, index);
		}

		if (node.getType() == Type.CloseObject) {
			compareCloseObject(node, node2, index);
		}
	}

	private void compareOneLineComment(Node node, Node node2, int index) {
		checkForTasks(node, node2, index);
	}

	private void compareCloseObject(Node node, Node node2, int index) {
		if (node2.getType() == Type.Comma || node2.getType() == Type.CloseArray || node2.getType() == Type.CloseObject) {
			checkForText(node, node2, index);
		} else {
			doError(node, node2, index);
		}
	}

	private void compareCloseArray(Node node, Node node2, int index) {

		if (node2.getType() == Type.Comma || node2.getType() == Type.CloseArray
				|| node2.getType() == Type.CloseObject) {
			checkForText(node, node2, index);
		} else {
			doError(node, node2, index);
		}

	}

	private void compareComma(Node node, Node node2, int index) {

		if (node2.getType() == Type.Quote || node2.getType() == Type.OpenArray
				|| node2.getType() == Type.OpenObject) {
			checkForText(node, node2, index);
		} else if (node2.getType() == Type.Comma || node2.getType() == Type.CloseArray) {
			buildValue(node, node2, index);
		} else {
			doError(node, node2, index);
		}
	}

	private void compareColon(Node node, Node node2, int index) {

		if (node2.getType() == Type.Comma || node2.getType() == Type.CloseObject) {
			buildValue(node, node2, index);
		} else if (node2.getType() == Type.Quote) {
			checkForText(node, node2, index);
		} else if (node2.getType() == Type.OpenArray
				|| node2.getType() == Type.OpenObject) {
			return;
		} else {
			doError(node, node2, index);
		}
	}

	private void compareOpenArray(Node node, Node node2, int index) {

		if (node2.getType() == Type.CloseArray || node2.getType() == Type.Comma) {
			buildValue(node, node2, index);
		} else if (node2.getType() == Type.Quote || node2.getType() == Type.OpenArray
				|| node2.getType() == Type.OpenObject ) {
			checkForText(node, node2, index);
		} else {
			doError(node, node2, index);
		}

	}
	private void compareNewLine(Node node, Node node2, int index) {
		if (node2.getType() == Type.NewLine) {
			doError(node, node2, index);
		}
	}
	private void compareOpenObject(Node node, Node node2, int index) {

		if (node2.getType() == Type.Quote || node2.getType() == Type.CloseObject) {
			checkForText(node, node2, index);
		} else {
			doError(node, node2, index);
		}
	}

	private void compareQuote(Node node, Node node2, int index) {

		if (node2.getType() == Type.Quote) {
			buildString(node, node2, index);
		} else if (node2.getType() == Type.Colon || node2.getType() == Type.Comma
				|| node2.getType() == Type.CloseArray || node2.getType() == Type.CloseObject) {
			checkForText(node, node2, index);
		} else {
			doError(node, node2, index);
		}

	}
	
	private void checkForTasks(Node node, Node node2, int index) {

		int offset = node.getEnd();
		int length = node2.getStart() - offset;

		try {
			String text = fDocument.get(offset, length);
			//System.out.println(text);
			if (text.contains("TODO")) {
				addNode(text, offset, length, index, Type.Todo);
			}
			if (text.contains("FIXME")) {
				addNode(text, offset, length, index, Type.Fixme);
			}
		} catch (BadLocationException e) {

		}
	}

	private void checkForText(Node node, Node node2, int index) {

		int offset = node.getEnd();
		int length = node2.getStart() - offset;

		try {
			String text = fDocument.get(offset, length);
			if (!text.trim().isEmpty()) {
				addErrorNode(text, offset, length, index);
			}
		} catch (BadLocationException e) {

		}
	}

	private void buildString(Node node, Node node2, int index) {

		int offset = node.getEnd();
		int length = node2.getStart() - offset;

		try {
			String text = fDocument.get(offset, length);
			if (text != null && !text.trim().isEmpty()) {
				addNode(text, offset, length, index, Type.String);
			}
		} catch (BadLocationException e) {

		}

	}

	private void buildValue(Node node, Node node2, int index) {

		int offset = node.getEnd();
		int length = node2.getStart() - offset;

		try {
			String text = fDocument.get(offset, length);
			String trimmedText = text.trim();

			if ("null".equals(trimmedText)) {
				addNode(trimmedText, offset, length, index, Type.Null);
			} else if ("true".equals(trimmedText)) {
				addNode(trimmedText, offset, length, index, Type.True);
			} else if ("false".equals(trimmedText)) {
				addNode(trimmedText, offset, length, index, Type.False);
			} else if (isANumber(trimmedText)) {
				addNode(trimmedText, offset, length, index, Type.Number);
			} else {
				checkForText(node, node2, index);
			}

		} catch (BadLocationException e) {
			return;
		}
	}

	private boolean isANumber(String text) {

		try {
			Float.parseFloat(text);
			return true;
		} catch (Exception e) {

		}

		return false;
	}

	private void addNode(String text, int offset, int length, int index, Type type) {
		Node node = new Node(type);
		node.setPosition(offset, length);
		node.setValue(text);
		nodes.add(index + 1, node);
	}

	private void doError(Node node, Node node2, int index) {

		int offset = node.getEnd();
		int length = node2.getStart() - offset;

		addErrorNode("Error", offset, length, index);
	}

	private void addErrorNode(String text, int offset, int length, int index) {

		Node errorNode = new Node(Type.Error);
		errorNode.setPosition(offset, length);
		errorNode.setValue(text.trim());
		nodes.add(index + 1, errorNode);
	//	errorNodeIndex.add(index + 1);
	}

	public static Type getCharType(char ch, char previous) {
		if (CharUtility.isNewLine(ch)) {
			return Type.NewLine;
		}
		
		if (CharUtility.isOneLineComment(ch, previous)) {
			return Type.OneLineComment;
		}
		
		if (ch == CharUtility.comma) {

			return Type.Comma;
		}

		if (ch == CharUtility.openCurly) {
			return Type.OpenObject;
		}

		if (ch == CharUtility.closeCurly) {
			return Type.CloseObject;
		}

		if (ch == CharUtility.openSquare) {
			return Type.OpenArray;
		}

		if (ch == CharUtility.closeSquare) {
			return Type.CloseArray;
		}


		if (ch == CharUtility.colon) {
			return Type.Colon;
		}

		if (ch == CharUtility.quote) {
			return Type.Quote;
		}

		return null;
	}
}
