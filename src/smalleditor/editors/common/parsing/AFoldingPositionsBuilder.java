package smalleditor.editors.common.parsing;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.tokenizer.DocumentNodeType;
import smalleditor.tokenizer.NodePosition;
import beaver.Scanner;
import beaver.Symbol;

public abstract class AFoldingPositionsBuilder {

	private Scanner scanner = null;
	private IDocument document;

	public AFoldingPositionsBuilder(IDocument document) {
		setDocument(document);
	}
	
	protected abstract Symbol getNextToken();
	protected abstract Boolean isOpenToken(Symbol token);
	protected abstract Boolean isCloseToken(Symbol token);
	protected abstract void setSource();
	protected abstract DocumentNodeType getNodeType(Symbol token);
	protected abstract boolean isTokenType(Symbol token, Object ... types);
	
	
	protected void setDocument(IDocument document) {
		this.document = document;
	}
	protected IDocument getDocument() {
		return document;
	}
	protected void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}
	protected Scanner getScanner() {
		return scanner;
	}

	public List<NodePosition> buildPositions() {
		setSource();
		
		List<NodePosition> positions = new LinkedList<NodePosition>();
		LinkedList<NodePosition> positionsStack = new LinkedList<NodePosition>();
		List<NodePosition> linesStack = new LinkedList<NodePosition>();
		Symbol token = null;
		int level = 0;
		while((token = getNextToken()) != null) {
//			System.out.println(token);
			if (isOpenToken(token)) {
				NodePosition position = new NodePosition(getStart(token));
				positionsStack.add(0, position);
				linesStack.add(0, new NodePosition(getLine(token)));
				position.setType(getNodeType(token));
				level++;
			} else if (isCloseToken(token) && positionsStack.size() > 0) {
				NodePosition position = positionsStack.remove(0);
				NodePosition line = linesStack.remove(0);
				if(line.getOffset() != getLine(token)) {
					position.setLength(getEnd(token) - position.getOffset());
					position.setLevel(level);
					positions.add(position);
				}
				level--;
			}
		}
		
		return positions;
	}
	
	private int getLine(Symbol token) {
		try {
			return document.getLineOfOffset(getStart(token));
		} catch (BadLocationException e) {
			System.err.println(token);
			e.printStackTrace();
		}
		return 0;
	}
	private int getStart(Symbol token) {
		return token.getStart();
	}

	private int getEnd(Symbol token) {
		if (token == null) {
			return 0;
		}
		return token.getEnd();
	}
}
